import { useEffect, useState } from "react";
import { getMyLearningSessions, createChatRoom, getRoomBySession } from "../../../api/api";
import ChatDialog from "../components/ChatDialog";
import { Button } from "@/components/ui/button";
import Loading from "../components/Loading";

export default function LearningSessions() {
    const [sessions, setSessions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [chatOpen, setChatOpen] = useState(false);
    const [roomId, setRoomId] = useState(null);

    // Fetch sessions for logged-in learner
    useEffect(() => {
        const fetchSessions = async () => {
            try {
                const res = await getMyLearningSessions();
                setSessions(res.data || []);
            } catch (err) {
                console.error("Failed to fetch sessions:", err);
            } finally {
                setLoading(false);
            }
        };
        fetchSessions();
    }, []);

    // Open chat: fetch room or create if not exists
    const openChat = async (session) => {
        try {
            // 1️⃣ Check if room exists for this session
            const res = await getRoomBySession(session.id);
            let room;

            if (res?.data) {
                // Room exists
                room = res.data;
            } else {
                // Room doesn’t exist → create it
                const roomData = {
                    swapSessionId: session.id,
                    userAId: session.user1Id, // learner ID
                    userBId: session.user2Id  // mentor ID
                };
                const createRes = await createChatRoom(roomData);
                room = createRes.data;
            }

            // 2️⃣ Open chat dialog
            setRoomId(room.id);
            setChatOpen(true);

        } catch (err) {
            console.error("Failed to open chat:", err);
            alert(err?.response?.data || "Failed to open chat");
        }
    };

    if (loading) return <Loading message="Loading sessions..." />;

    return (
        <div className="min-h-screen max-w-5xl mx-auto m-10 px-4">
            <h2 className="text-2xl font-bold mb-6">My Learning Sessions</h2>

            {sessions.length === 0 ? (
                <p className="text-gray-400">No sessions found</p>
            ) : (
                <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-5">
                    {sessions.map(session => (
                        <div
                            key={session.id}
                            className="bg-white border rounded-xl p-5 shadow hover:shadow-lg transition"
                        >
                            <div className="space-y-2 mb-3">
                                <p className="text-sm">
                                    <span className="font-semibold text-indigo-600">Skill:</span> {session.skill || "Not selected"}
                                </p>
                                <p className="text-sm">
                                    <span className="font-semibold text-green-600">Scheduled Time:</span>{" "}
                                    {session.scheduledTime ? new Date(session.scheduledTime).toLocaleString() : "Not scheduled"}
                                </p>
                                <p className="text-sm">
                                    <span className="font-semibold text-purple-600">Tokens:</span> {session.tokenAmount}
                                </p>
                            </div>

                            <p className="text-xs mb-4">
                                Status:{" "}
                                <span
                                    className={`ml-2 font-semibold ${session.status === "active"
                                            ? "text-green-600"
                                            : session.status === "rejected"
                                                ? "text-red-600"
                                                : "text-yellow-600"
                                        }`}
                                >
                                    {session.status}
                                </span>
                            </p>

                            {session.status === "active" && (
                                <Button
                                    size="sm"
                                    className="bg-indigo-600 hover:bg-indigo-700"
                                    onClick={() => openChat(session)}
                                >
                                    Chat
                                </Button>
                            )}
                        </div>
                    ))}
                </div>
            )}

            {/* Chat Dialog */}
            <ChatDialog
                open={chatOpen}
                setOpen={setChatOpen}
                roomId={roomId}
                userId={localStorage.getItem("userId")}
            />
        </div>
    );
}