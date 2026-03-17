import { useEffect, useState } from "react";
import { getMyTeachingSessions, updateSessionStatus, createChatRoom } from "../../../api/api";
import { Button } from "@/components/ui/button";
import Loading from "../components/Loading";
import ChatDialog from "./ChatPage";

export default function MentorSessions() {
    const [sessions, setSessions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [chatOpen, setChatOpen] = useState(false);
    const [roomId, setRoomId] = useState(null);
    const [createdRooms, setCreatedRooms] = useState({});

    const createRoomAndOpenChat = async (session) => {
        try {
            const roomData = {
                swapSessionId: session.id,
                userAId: session.user1Id, // learner ID
                userBId: session.user2Id  // mentor ID
            };

            console.log("Creating room with:", roomData);

            const res = await createChatRoom(roomData);

            if (res?.data) {
                console.log("Room created:", res.data);

                // Set roomId and open chat
                setRoomId(res.data.id);   // assuming backend returns created room with `id`
                setChatOpen(true);
            }

        } catch (error) {
            console.error("Failed to create chat room:", error);
            alert(error?.response?.data || "Failed to create chat room");
        }
    };

    const createRoom = async (session) => {
        try {
            const roomData = {
                swapSessionId: session.id,
                userAId: session.user1Id, // learner ID
                userBId: session.user2Id  // mentor ID
            };

            console.log("Creating room with:", roomData);

            const res = await createChatRoom(roomData);

            if (res?.data) {
                alert("Chat room created successfully");
                console.log("Room:", res.data);

                // Mark this session as having a room
                setCreatedRooms(prev => ({ ...prev, [session.id]: res.data.id }));
            }

        } catch (error) {
            console.error("Failed to create chat room:", error);
            alert(error?.response?.data || "Failed to create chat room");
        }
    };

    // Fetch sessions for logged-in mentor
    useEffect(() => {
        const fetchSessions = async () => {
            try {
                const res = await getMyTeachingSessions();
                setSessions(res.data || []);
            } catch (error) {
                console.error("Failed to fetch sessions:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchSessions();
    }, []);

    // Update session status
    const handleUpdateStatus = async (sessionId, status) => {
        try {
            await updateSessionStatus(sessionId, status);
            // Update local state
            setSessions(prev =>
                prev.map(s => (s.id === sessionId ? { ...s, status } : s))
            );
        } catch (error) {
            console.error("Update failed:", error);
        }
    };

    if (loading) return <Loading message="Loading sessions..." />;

    return (
        <div className="min-h-screen max-w-5xl mx-auto m-10 px-4">
            <h2 className="text-2xl font-bold mb-6">Skill Swap Sessions</h2>

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
                                    <span className="font-semibold text-indigo-600">
                                        Skill:
                                    </span>{" "}
                                    {session.skill || "Not selected"}
                                </p>

                                <p className="text-sm">
                                    <span className="font-semibold text-green-600">
                                        Scheduled Time:
                                    </span>{" "}
                                    {session.scheduledTime
                                        ? new Date(session.scheduledTime).toLocaleString()
                                        : "Not scheduled"}
                                </p>

                                <p className="text-sm">
                                    <span className="font-semibold text-purple-600">
                                        Tokens:
                                    </span>{" "}
                                    {session.tokenAmount}
                                </p>
                            </div>

                            <p className="text-xs mb-4">
                                Status:
                                <span
                                    className={`ml-2 font-semibold 
                                        ${session.status === "active"
                                            ? "text-green-600"
                                            : session.status === "rejected"
                                                ? "text-red-600"
                                                : "text-yellow-600"
                                        }`}
                                >
                                    {session.status}
                                </span>
                            </p>

                            <div className="flex gap-2">

                                {session.status === "pending" && (
                                    <>
                                        <Button
                                            size="sm"
                                            className="bg-green-600 hover:bg-green-700"
                                            onClick={() => handleUpdateStatus(session.id, "active")}
                                        >
                                            Accept
                                        </Button>

                                        <Button
                                            size="sm"
                                            variant="destructive"
                                            onClick={() => handleUpdateStatus(session.id, "rejected")}
                                        >
                                            Reject
                                        </Button>
                                    </>
                                )}

                                {session.status === "active" && (
                                    <>
                                        <Button
                                            size="sm"
                                            className="bg-blue-600 hover:bg-blue-700"
                                            onClick={() => handleUpdateStatus(session.id, "completed")}
                                        >
                                            Complete
                                        </Button>

                                        {createdRooms[session.id] ? (
                                            <Button
                                                size="sm"
                                                className="bg-indigo-600 hover:bg-indigo-700"
                                                onClick={() => {
                                                    setRoomId(createdRooms[session.id]);
                                                    setChatOpen(true);
                                                }}
                                            >
                                                Chat
                                            </Button>
                                        ) : (
                                            <Button
                                                size="sm"
                                                className="bg-indigo-600 hover:bg-indigo-700"
                                                onClick={() => createRoom(session)}
                                            >
                                                Create Room
                                            </Button>
                                        )}
                                    </>
                                )}

                            </div>
                        </div>
                    ))}
                </div>
            )}

            <ChatDialog
                open={chatOpen}
                setOpen={setChatOpen}
                roomId={roomId}
                userId={localStorage.getItem("userId")}
            />
        </div>
    );
}


