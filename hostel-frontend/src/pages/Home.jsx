import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function Home() {
    const navigate = useNavigate();

    const [hostels, setHostels] = useState([]);
    const [keyword, setKeyword] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // Load hostels from the backend.
    async function fetchHostels(searchKeyword = "") {
        setLoading(true);
        setError("");

        try {
            let response;

            if (searchKeyword.trim()) {
                response = await api.get("/hostels/search", {
                    params: {
                        keyword: searchKeyword.trim(),
                    },
                });
            } else {
                response = await api.get("/hostels");
            }

            setHostels(response.data);
        } catch (err) {
            setError(
                err.response?.data?.message ||
                    err.response?.data?.error ||
                    "Unable to load hostels. Please check your backend."
            );
        } finally {
            setLoading(false);
        }
    }

    // Check login and load hostels when the page opens.
    useEffect(() => {
        if (!localStorage.getItem("token")) {
            navigate("/login", { replace: true });
            return;
        }

        fetchHostels();
    }, [navigate]);

    // Search by hostel name or location.
    function handleSearch(event) {
        event.preventDefault();
        fetchHostels(keyword);
    }

    // Clear search and reload every hostel.
    function handleResetSearch() {
        setKeyword("");
        fetchHostels("");
    }

    // Log out the current user.
    function handleLogout() {
        localStorage.removeItem("token");
        navigate("/login", { replace: true });
    }

    return (
        <div className="home-container">
            {/* Navigation bar */}
            <header className="home-header">
                <h2>Hostel Management</h2>

                <button
                    type="button"
                    className="logout-button"
                    onClick={handleLogout}
                >
                    Logout
                </button>
            </header>

            <main className="home-content">
                {/* Welcome section and search */}
                <section className="home-hero">
                    <p className="eyebrow">
                        YOUR STAY, YOUR CHOICE
                    </p>

                    <h1>Find Your Ideal Student Hostel</h1>

                    <p>
                        Explore student hostels, compare monthly rents,
                        and discover accommodation that suits you.
                    </p>

                    <form
                        className="search-form"
                        onSubmit={handleSearch}
                    >
                        <input
                            type="search"
                            placeholder="Search hostel name or keyword"
                            value={keyword}
                            onChange={(event) =>
                                setKeyword(event.target.value)
                            }
                            aria-label="Search hostels"
                        />

                        <button type="submit" disabled={loading}>
                            {loading ? "Searching..." : "Search"}
                        </button>
                    </form>

                    <button
                        type="button"
                        className="reset-search"
                        onClick={handleResetSearch}
                        disabled={loading}
                    >
                        Show all hostels
                    </button>
                </section>

                {/* Hostel listings */}
                <section className="hostel-section">
                    <div className="hostel-section-heading">
                        <h2>Available Hostels</h2>

                        {!loading && !error && (
                            <span className="hostel-count">
                                {hostels.length}{" "}
                                {hostels.length === 1
                                    ? "hostel"
                                    : "hostels"}
                            </span>
                        )}
                    </div>

                    {loading && (
                        <p className="status-message">
                            Loading hostels...
                        </p>
                    )}

                    {!loading && error && (
                        <div className="error-message">
                            <p>{error}</p>

                            <button
                                type="button"
                                onClick={() => fetchHostels(keyword)}
                            >
                                Try Again
                            </button>
                        </div>
                    )}

                    {!loading && !error && hostels.length === 0 && (
                        <div className="empty-state">
                            <h3>No hostels found</h3>
                            <p>
                                Try another keyword or check back when
                                hostel listings are available.
                            </p>
                            {keyword.trim() && (
                                <button
                                    type="button"
                                    onClick={handleResetSearch}
                                >
                                    Clear Search
                                </button>
                            )}
                        </div>
                    )}

                    {!loading && !error && hostels.length > 0 && (
                        <div className="hostel-grid">
                            {hostels.map((hostel) => (
                                <article
                                    className="hostel-card"
                                    key={hostel.id}
                                >
                                    <div className="hostel-card-banner">
                                        <span>
                                            Student Accommodation
                                        </span>
                                    </div>

                                    <div className="hostel-card-content">
                                        <h3>
                                            {hostel.name ||
                                                "Unnamed Hostel"}
                                        </h3>

                                        <p className="hostel-location">
                                            <span aria-hidden="true">
                                                📍{" "}
                                            </span>
                                            {[
                                                hostel.area,
                                                hostel.city,
                                            ]
                                                .filter(Boolean)
                                                .join(", ") ||
                                                "Location not provided"}
                                        </p>

                                        {hostel.address && (
                                            <p className="hostel-description">
                                                {hostel.address}
                                            </p>
                                        )}

                                        {hostel.monthlyRent != null && (
                                            <p className="hostel-rent">
                                                ₹
                                                {Number(
                                                    hostel.monthlyRent
                                                ).toLocaleString("en-IN")}
                                                <span> / month</span>
                                            </p>
                                        )}

                                        {hostel.description && (
                                            <p className="hostel-description">
                                                {hostel.description}
                                            </p>
                                        )}

                                        {hostel.contactNumber && (
                                            <p className="hostel-contact">
                                                Contact:{" "}
                                                {hostel.contactNumber}
                                            </p>
                                        )}

                                        <button
                                            type="button"
                                            className="details-button"
                                            onClick={() =>
                                                navigate(
                                                    `/hostels/${hostel.id}`
                                                )
                                            }
                                        >
                                            View Details
                                        </button>
                                    </div>
                                </article>
                            ))}
                        </div>
                    )}
                </section>
            </main>
        </div>
    );
}

export default Home;