import {
    BrowserRouter,
    Routes,
    Route,
    Navigate,
} from "react-router-dom";

import Register from "./pages/Register";
import Login from "./pages/Login";
import Home from "./pages/Home";
import HostelDetails from "./pages/HostelDetails";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/hostels/:id" element={<HostelDetails />} />
                <Route
                    path="*"
                    element={<Navigate to="/login" replace />}
                />
            </Routes>
        </BrowserRouter>
    );
}

export default App;