
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api";

function Login() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        email: "",
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
            const response = await api.post(
                "/auth/login",
                formData
            );

            const { token, userId, role } = response.data;

            if (!token) {
                throw new Error(
                    "The server did not return a login token."
                );
            }

            if (userId == null) {
                throw new Error(
                    "The server did not return your user ID. " +
                    "Please restart the backend and try again."
                );
            }

            if (
                role !== "NEW_STUDENT" &&
                role !== "VERIFIED_OLD_STUDENT"
            ) {
                throw new Error(
                    "The server returned an invalid student role."
                );
            }

            // Store the authenticated session details.
            localStorage.setItem("token", token);
            localStorage.setItem("userId", String(userId));
            localStorage.setItem("role", role);

            setMessage("Login successful!");

            navigate("/", { replace: true });

        } catch (error) {
            localStorage.removeItem("token");
            localStorage.removeItem("userId");
            localStorage.removeItem("role");

            setMessage(
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Login failed. Please check your email and password."
            );
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="auth-container">
            <form
                className="auth-card"
                onSubmit={handleSubmit}
            >
                <h1>Welcome Back</h1>

                <p className="auth-subtitle">
                    Login to explore student hostels and reviews.
                </p>

                <label htmlFor="email">
                    Email Address
                </label>

                <input
                    id="email"
                    type="email"
                    name="email"
                    placeholder="Enter your email"
                    value={formData.email}
                    onChange={handleChange}
                    autoComplete="email"
                    required
                />

                <label htmlFor="password">
                    Password
                </label>

                <input
                    id="password"
                    type="password"
                    name="password"
                    placeholder="Enter your password"
                    value={formData.password}
                    onChange={handleChange}
                    autoComplete="current-password"
                    required
                />

                <button
                    type="submit"
                    disabled={loading}
                >
                    {loading ? "Logging in..." : "Login"}
                </button>

                {message && (
                    <p
                        className="auth-message"
                        role="status"
                    >
                        {message}
                    </p>
                )}

                <p className="auth-footer">
                    Don't have an account?{" "}
                    <Link to="/register">
                        Register
                    </Link>
                </p>
            </form>
        </div>
    );
}

export default Login;