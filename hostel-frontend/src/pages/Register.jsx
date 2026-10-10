import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api";

function Register() {
const navigate = useNavigate();


const [formData, setFormData] = useState({
    name: "",
    rollNumber: "",
    email: "",
    mobile: "",
    password: "",
});

const [message, setMessage] = useState("");
const [loading, setLoading] = useState(false);

function handleChange(event) {
    setFormData({
        ...formData,
        [event.target.name]: event.target.value,
    });
}

async function handleSubmit(event) {
    event.preventDefault();
    setMessage("");
    setLoading(true);

    try {
        const response = await api.post("/auth/register", formData);

        setMessage(
            response.data?.message || "Registration successful! Redirecting to login..."
        );

        setTimeout(() => {
            navigate("/login");
        }, 1200);
    } catch (error) {
        const errorMessage =
            error.response?.data?.message ||
            error.response?.data?.error ||
            "Registration failed. Please check your details and try again.";

        setMessage(errorMessage);
    } finally {
        setLoading(false);
    }
}

return (
    <div className="auth-container">
        <form className="auth-card" onSubmit={handleSubmit}>
            <h1>Create Account</h1>
            <p className="auth-subtitle">
                Join Hostel Management to explore student hostels.
            </p>

            <label htmlFor="name">Full Name</label>
            <input
                id="name"
                type="text"
                name="name"
                placeholder="Enter your full name"
                value={formData.name}
                onChange={handleChange}
                required
            />

            
            <label htmlFor="rollNumber">
                University Roll Number (Optional)
            </label>
            <input
                id="rollNumber"
                type="text"
                name="rollNumber"
                placeholder="e.g. 26eg109a38 (optional)"
                value={formData.rollNumber}
                onChange={handleChange}
                pattern="[0-9]{2}[eE][gG][0-9]+[a-zA-Z][0-9]+"
                title="If provided, use a format like 26eg109a38"
            />

            <label htmlFor="email">Email Address</label>
            <input
                id="email"
                type="email"
                name="email"
                placeholder="Enter your email"
                value={formData.email}
                onChange={handleChange}
                required
            />

            
            <label htmlFor="mobile">Mobile Number (Optional)</label>
            <input
                id="mobile"
                type="tel"
                name="mobile"
                placeholder="Enter your mobile number (optional)"
                value={formData.mobile}
                onChange={handleChange}
            />

            <label htmlFor="password">Password</label>
            <input
                id="password"
                type="password"
                name="password"
                placeholder="Create a password"
                value={formData.password}
                onChange={handleChange}
                required
                minLength={8}
            />

            <button type="submit" disabled={loading}>
                {loading ? "Creating Account..." : "Register"}
            </button>

            {message && (
                <p className="auth-message" role="status">
                    {message}
                </p>
            )}

            <p className="auth-footer">
                Already have an account? <Link to="/login">Login</Link>
            </p>
        </form>
    </div>
);


}

export default Register;
