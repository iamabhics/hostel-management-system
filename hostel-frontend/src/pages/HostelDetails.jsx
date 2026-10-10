
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";

const categories = [
    ["Food", "food"],
    ["Cleanliness", "cleanliness"],
    ["Room", "room"],
    ["Facilities", "facilities"],
    ["Safety", "safety"],
    ["Staff Behaviour", "staffBehaviour"],
    ["Value for Money", "valueForMoney"],
    ["Maintenance", "maintenance"],
];

const createEmptyForm = () => ({
    title: "",
    description: "",
    overallRating: "",
    food: "",
    cleanliness: "",
    room: "",
    facilities: "",
    safety: "",
    staffBehaviour: "",
    valueForMoney: "",
    maintenance: "",
});

function HostelDetails() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [hostel, setHostel] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [form, setForm] = useState(createEmptyForm);
    const [editingReviewId, setEditingReviewId] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const [deletingReviewId, setDeletingReviewId] = useState(null);
    const [submitMessage, setSubmitMessage] = useState("");
    const [submitError, setSubmitError] = useState("");

    const role = localStorage.getItem("role");
    const canWriteReview = role === "VERIFIED_OLD_STUDENT";

    function getErrorMessage(err, fallback) {
        const data = err.response?.data;

        if (typeof data === "string" && data.trim()) {
            return data;
        }

        return (
            data?.message ||
            data?.error ||
            fallback
        );
    }

    async function loadReviews() {
        const response = await api.get(`/hostels/${id}/reviews`);
        setReviews(response.data);
    }

    useEffect(() => {
        if (!localStorage.getItem("token")) {
            navigate("/login", { replace: true });
            return;
        }

        async function loadDetails() {
            setLoading(true);
            setError("");

            try {
                const [hostelResponse, reviewsResponse] =
                    await Promise.all([
                        api.get(`/hostels/${id}`),
                        api.get(`/hostels/${id}/reviews`),
                    ]);

                setHostel(hostelResponse.data);
                setReviews(reviewsResponse.data);
            } catch (err) {
                setError(
                    getErrorMessage(
                        err,
                        "Unable to load hostel details. Please try again."
                    )
                );
            } finally {
                setLoading(false);
            }
        }

        loadDetails();
    }, [id, navigate]);

    function handleChange(event) {
        const { name, value } = event.target;

        setForm((previous) => ({
            ...previous,
            [name]: value,
        }));

        setSubmitMessage("");
        setSubmitError("");
    }

    function resetForm() {
        setForm(createEmptyForm());
        setEditingReviewId(null);
        setSubmitMessage("");
        setSubmitError("");
    }

    function handleEdit(review) {
        if (!canWriteReview) {
            setSubmitError(
                "Only verified old students can edit reviews."
            );
            return;
        }

        setEditingReviewId(review.id);

        setForm({
            title: review.title ?? "",
            description: review.description ?? "",
            overallRating:
                review.overallRating == null
                    ? ""
                    : String(review.overallRating),
            food: review.food == null ? "" : String(review.food),
            cleanliness:
                review.cleanliness == null
                    ? ""
                    : String(review.cleanliness),
            room: review.room == null ? "" : String(review.room),
            facilities:
                review.facilities == null
                    ? ""
                    : String(review.facilities),
            safety: review.safety == null ? "" : String(review.safety),
            staffBehaviour:
                review.staffBehaviour == null
                    ? ""
                    : String(review.staffBehaviour),
            valueForMoney:
                review.valueForMoney == null
                    ? ""
                    : String(review.valueForMoney),
            maintenance:
                review.maintenance == null
                    ? ""
                    : String(review.maintenance),
        });

        setSubmitMessage("");
        setSubmitError("");

        document.getElementById("review-form-section")
            ?.scrollIntoView({ behavior: "smooth" });
    }

    async function handleDelete(review) {
        if (!canWriteReview) {
            setSubmitError(
                "Only verified old students can delete reviews."
            );
            return;
        }

        const confirmed = window.confirm(
            "Are you sure you want to permanently delete this review?"
        );

        if (!confirmed) {
            return;
        }

        setDeletingReviewId(review.id);
        setSubmitMessage("");
        setSubmitError("");

        try {
            await api.delete(`/reviews/${review.id}`);

            if (editingReviewId === review.id) {
                resetForm();
            }

            await loadReviews();
            setSubmitMessage("Review deleted successfully.");
        } catch (err) {
            setSubmitError(
                getErrorMessage(
                    err,
                    "Unable to delete the review. Please try again."
                )
            );
        } finally {
            setDeletingReviewId(null);
        }
    }

    async function handleSubmit(event) {
        event.preventDefault();

        if (!canWriteReview) {
            setSubmitError(
                "Only verified old students can submit reviews."
            );
            return;
        }

        setSubmitting(true);
        setSubmitMessage("");
        setSubmitError("");

        const ratingFields = categories.map(([, field]) => field);

        const payload = {
            title: form.title.trim(),
            description: form.description.trim(),
            overallRating:
                form.overallRating === ""
                    ? null
                    : Number(form.overallRating),
        };

        for (const field of ratingFields) {
            payload[field] =
                form[field] === "" ? null : Number(form[field]);
        }

        if (!payload.title && !payload.description) {
            setSubmitError(
                "Please enter a review title or a written description."
            );
            setSubmitting(false);
            return;
        }

        if (payload.title.length > 100) {
            setSubmitError(
                "Review title cannot exceed 100 characters."
            );
            setSubmitting(false);
            return;
        }

        if (payload.description.length > 1000) {
            setSubmitError(
                "Review description cannot exceed 1000 characters."
            );
            setSubmitting(false);
            return;
        }

        const allRatings = [
            payload.overallRating,
            ...ratingFields.map((field) => payload[field]),
        ];

        if (
            allRatings.some(
                (rating) =>
                    rating !== null &&
                    (!Number.isFinite(rating) ||
                        rating < 0 ||
                        rating > 10)
            )
        ) {
            setSubmitError("Ratings must be between 0 and 10.");
            setSubmitting(false);
            return;
        }

        try {
            if (editingReviewId !== null) {
                await api.put(
                    `/reviews/${editingReviewId}`,
                    payload
                );

                setSubmitMessage("Review updated successfully.");
            } else {
                await api.post(`/hostels/${id}/reviews`, payload);

                setSubmitMessage(
                    "Your review was submitted successfully."
                );
            }

            setForm(createEmptyForm());
            setEditingReviewId(null);

            await loadReviews();
        } catch (err) {
            setSubmitError(
                getErrorMessage(
                    err,
                    editingReviewId !== null
                        ? "Unable to update your review. Please try again."
                        : "Unable to submit your review. Please try again."
                )
            );
        } finally {
            setSubmitting(false);
        }
    }

    function handleLogout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("role");

    navigate("/login", { replace: true });
    }

    if (loading) {
        return (
            <main className="details-page">
                <p>Loading hostel details...</p>
            </main>
        );
    }

    if (error) {
        return (
            <main className="details-page">
                <button onClick={() => navigate("/")}>
                    ← Back to Hostels
                </button>
                <p className="error-message">{error}</p>
            </main>
        );
    }

    if (!hostel) {
        return (
            <main className="details-page">
                <p>Hostel not found.</p>
                <button onClick={() => navigate("/")}>
                    Back to Hostels
                </button>
            </main>
        );
    }

    const ratedReviews = reviews.filter(
        (review) =>
            review.overallRating != null &&
            Number.isFinite(Number(review.overallRating))
    );

    const averageRating = ratedReviews.length
        ? (
            ratedReviews.reduce(
                (total, review) =>
                    total + Number(review.overallRating),
                0
            ) / ratedReviews.length
        ).toFixed(1)
        : null;

    return (
        <div className="details-page">
            <header className="details-header">
                <button
                    type="button"
                    onClick={() => navigate("/")}
                >
                    ← Back to Hostels
                </button>

                <button type="button" onClick={handleLogout}>
                    Logout
                </button>
            </header>

            <main className="details-content">
                <section className="details-hostel-card">
                    <p className="eyebrow">STUDENT ACCOMMODATION</p>

                    <h1>{hostel.name}</h1>

                    <p>
                        📍{" "}
                        {[hostel.area, hostel.city]
                            .filter(Boolean)
                            .join(", ") || "Location not provided"}
                    </p>

                    {hostel.address && <p>{hostel.address}</p>}

                    {hostel.monthlyRent != null && (
                        <h2>
                            ₹{Number(hostel.monthlyRent).toLocaleString("en-IN")}
                            <span className="rent-period"> / month</span>
                        </h2>
                    )}

                    {hostel.description && <p>{hostel.description}</p>}

                    {hostel.contactNumber && (
                        <p>Contact: {hostel.contactNumber}</p>
                    )}
                </section>

                <section className="details-reviews-section">
                    <h2>Student Reviews</h2>

                    <div className="rating-summary">
                        <h3>
                            {averageRating === null
                                ? "No ratings yet"
                                : `${averageRating} / 10`}
                        </h3>

                        <p>
                            {ratedReviews.length}{" "}
                            {ratedReviews.length === 1
                                ? "rated review"
                                : "rated reviews"}
                            {" · "}
                            {reviews.length} total{" "}
                            {reviews.length === 1 ? "review" : "reviews"}
                        </p>
                    </div>

                    {submitError && (
                        <p className="error-message" role="alert">
                            {submitError}
                        </p>
                    )}

                    {submitMessage && (
                        <p role="status">{submitMessage}</p>
                    )}

                    {reviews.length === 0 ? (
                        <div className="empty-state">
                            <h3>No reviews yet</h3>
                            <p>
                                No student reviews have been submitted
                                for this hostel yet.
                            </p>
                        </div>
                    ) : (
                        <div className="review-list">
                            {reviews.map((review) => {
                                // Ownership is checked by the backend.
                                // This comparison only controls button visibility.
                                const isOwner =
                                    canWriteReview &&
                                    String(review.userId) ===
                                        localStorage.getItem("userId");

                                return (
                                    <article
                                        className="review-card"
                                        key={review.id}
                                    >
                                        <div className="review-card-heading">
                                            <h3>
                                                {review.title || "Student Review"}
                                            </h3>

                                            <strong>
                                                {review.overallRating ?? "—"}
                                                {review.overallRating != null
                                                    ? " / 10"
                                                    : ""}
                                            </strong>
                                        </div>

                                        {review.createdAt && (
                                            <p className="review-date">
                                                Posted:{" "}
                                                {new Date(
                                                    review.createdAt
                                                ).toLocaleDateString()}
                                            </p>
                                        )}

                                        {review.updatedAt &&
                                            review.updatedAt !== review.createdAt && (
                                                <p className="review-date">
                                                    Updated:{" "}
                                                    {new Date(
                                                        review.updatedAt
                                                    ).toLocaleDateString()}
                                                </p>
                                            )}

                                        <p>
                                            {review.description ||
                                                "No written description provided."}
                                        </p>

                                        <h4>Category Ratings</h4>

                                        <div className="category-ratings">
                                            {categories.map(([label, field]) => (
                                                <div
                                                    className="category-rating"
                                                    key={field}
                                                >
                                                    <span>{label}</span>
                                                    <strong>
                                                        {review[field] ?? "—"}
                                                        {review[field] != null
                                                            ? " / 10"
                                                            : ""}
                                                    </strong>
                                                </div>
                                            ))}
                                        </div>

                                        {isOwner && (
                                            <div className="review-actions">
                                                <button
                                                    type="button"
                                                    onClick={() =>
                                                        handleEdit(review)
                                                    }
                                                    disabled={
                                                        submitting ||
                                                        deletingReviewId !== null
                                                    }
                                                >
                                                    Edit
                                                </button>

                                                <button
                                                    type="button"
                                                    onClick={() =>
                                                        handleDelete(review)
                                                    }
                                                    disabled={
                                                        submitting ||
                                                        deletingReviewId === review.id
                                                    }
                                                >
                                                    {deletingReviewId === review.id
                                                        ? "Deleting..."
                                                        : "Delete"}
                                                </button>
                                            </div>
                                        )}
                                    </article>
                                );
                            })}
                        </div>
                    )}
                </section>

                {canWriteReview ? (
                    <section
                        className="details-reviews-section"
                        id="review-form-section"
                    >
                        <h2>
                            {editingReviewId !== null
                                ? "Edit Your Review"
                                : "Write a Review"}
                        </h2>

                        <p>
                            Share your experience. You can leave written
                            feedback without giving ratings, or rate any
                            categories you wish.
                        </p>

                        <form
                            className="review-form"
                            onSubmit={handleSubmit}
                        >
                            <label htmlFor="review-title">
                                Review Title (optional)
                            </label>
                            <input
                                id="review-title"
                                name="title"
                                type="text"
                                maxLength={100}
                                value={form.title}
                                onChange={handleChange}
                                placeholder="e.g. Comfortable stay and helpful staff"
                            />
                            <small>{form.title.length}/100 characters</small>

                            <label htmlFor="review-description">
                                Your Review
                            </label>
                            <textarea
                                id="review-description"
                                name="description"
                                rows={5}
                                maxLength={1000}
                                value={form.description}
                                onChange={handleChange}
                                placeholder="Describe your experience at this hostel..."
                            />
                            <small>
                                {form.description.length}/1000 characters
                            </small>

                            <h3>Optional Ratings (0–10)</h3>
                            <p>
                                Leave any category blank if you don't want
                                to rate it.
                            </p>

                            <div className="review-rating-fields">
                                {categories.map(([label, field]) => (
                                    <div key={field}>
                                        <label htmlFor={`rating-${field}`}>
                                            {label}
                                        </label>
                                        <input
                                            id={`rating-${field}`}
                                            name={field}
                                            type="number"
                                            min="0"
                                            max="10"
                                            step="0.5"
                                            value={form[field]}
                                            onChange={handleChange}
                                            placeholder="Skip"
                                        />
                                    </div>
                                ))}
                            </div>

                            <label htmlFor="overall-rating">
                                Overall Rating (optional)
                            </label>
                            <input
                                id="overall-rating"
                                name="overallRating"
                                type="number"
                                min="0"
                                max="10"
                                step="0.5"
                                value={form.overallRating}
                                onChange={handleChange}
                                placeholder="Leave blank to calculate from category ratings"
                            />

                            {submitError && (
                                <p className="error-message" role="alert">
                                    {submitError}
                                </p>
                            )}

                            {submitMessage && (
                                <p role="status">{submitMessage}</p>
                            )}

                            <button
                                type="submit"
                                disabled={submitting}
                            >
                                {submitting
                                    ? editingReviewId !== null
                                        ? "Updating..."
                                        : "Submitting..."
                                    : editingReviewId !== null
                                        ? "Save Changes"
                                        : "Submit Review"}
                            </button>

                            {editingReviewId !== null && (
                                <button
                                    type="button"
                                    onClick={resetForm}
                                    disabled={submitting}
                                >
                                    Cancel Edit
                                </button>
                            )}
                        </form>
                    </section>
                ) : (
                    <section className="details-reviews-section">
                        <h2>Write a Review</h2>
                        <p>
                            Only verified old students can submit reviews.
                            You can still browse and read all hostel reviews.
                        </p>
                    </section>
                )}
            </main>
        </div>
    );
}

export default HostelDetails;