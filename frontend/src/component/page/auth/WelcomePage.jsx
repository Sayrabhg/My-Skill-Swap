import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { updateProfile } from "../../../api/api"; // your API function

export default function WelcomePage() {
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem("user")) || {};
    const [accepted, setAccepted] = useState(false);
    const [loading, setLoading] = useState(false);

    const handleGetStarted = async () => {
        if (!user?.id) return;

        if (!accepted) {
            window.alert("Please accept our Privacy Policy and Terms to continue.");
            return;
        }

        setLoading(true);
        try {
            // Update firstLogin to false in backend
            await updateProfile({ isFirstLogin: false });

            // Update localStorage so subsequent logins go to dashboard
            const updatedUser = { ...user, isFirstLogin: false };
            localStorage.setItem("user", JSON.stringify(updatedUser));

            // Navigate to dashboard
            navigate("/dashboard");
        } catch (error) {
            console.error("Failed to update firstLogin:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-indigo-50 to-white flex items-center justify-center px-4">
            <div className="relative max-w-3xl w-full bg-white shadow-xl rounded-2xl p-8 text-center">
                <h1 className="text-3xl font-bold text-gray-800 mb-4">
                    Welcome to Skill Swap 👋
                </h1>

                <p className="text-gray-500 mb-6">
                    {user?.name ? `Hi ${user.name},` : "Hi there,"}{" "}
                    welcome to Skill Swap! 🚀 Get ready to learn new skills, share your expertise,
                    and connect with a vibrant community of learners and mentors.
                    Let's start swapping skills and growing together!
                </p>

                {/* Checkbox */}
                <div className="mb-6 flex items-center justify-center gap-2">
                    <input
                        type="checkbox"
                        id="privacyCheck"
                        checked={accepted}
                        onChange={() => setAccepted(!accepted)}
                        className="w-4 h-4 accent-indigo-600"
                    />
                    <label htmlFor="privacyCheck" className="text-gray-500 text-sm">
                        I agree to the <span className="text-indigo-600 font-medium">Privacy Policy</span> and{" "}
                        <span className="text-indigo-600 font-medium">Terms of Service</span>.
                    </label>
                </div>

                <Button
                    type="button"
                    onClick={handleGetStarted}
                    disabled={!accepted || loading}
                    className={`px-6 py-2 rounded-lg text-white ${accepted && !loading ? "bg-indigo-600 hover:bg-indigo-700" : "bg-gray-400 cursor-not-allowed"
                        }`}
                >
                    {loading ? "Processing..." : "Get Started →"}
                </Button>
            </div>
        </div>
    );
}