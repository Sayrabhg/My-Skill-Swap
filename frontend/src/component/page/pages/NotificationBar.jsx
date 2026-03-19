import { useEffect, useState, useRef } from "react";
import { Bell, X } from "lucide-react";
import { useNavigate } from "react-router-dom";
import {
    getUserById,
    getUserChatRooms,
    getChatMessages
} from "../../../api/api";

export default function NotificationBar({ loggedInUser }) {
    const [notifications, setNotifications] = useState([]);
    const [modalOpen, setModalOpen] = useState(false);
    const [userId, setUserId] = useState(null);
    const modalRef = useRef(null);

    const navigate = useNavigate();

    // ✅ Set logged user
    useEffect(() => {
        if (loggedInUser) setUserId(loggedInUser.id);
        else setUserId(null);
    }, [loggedInUser]);

    // ✅ FETCH NOTIFICATIONS WITH LAST MESSAGE + TIME
    const fetchNotifications = async () => {
        if (!userId) return;

        try {
            const res = await getUserChatRooms();
            const rooms = res.data;

            const notifs = await Promise.all(
                rooms.map(async (room) => {
                    const otherUserId =
                        room.userAId === userId
                            ? room.userBId
                            : room.userAId;

                    let otherUserName = "Unknown User";
                    let avatar = "";
                    let lastMessage = "";
                    let lastMessageTime = "";

                    // ✅ Fetch user (name + avatar)
                    try {
                        const userRes = await getUserById(otherUserId);
                        otherUserName = userRes.data.user.name;
                        avatar = userRes.data.user.avatar; // ✅ ADD THIS
                    } catch (err) {
                        console.error("User fetch failed:", err);
                    }

                    // ✅ Fetch last message
                    try {
                        const msgRes = await getChatMessages(room.id);
                        const messages = msgRes.data || [];

                        if (messages.length > 0) {
                            const lastMsg = messages[messages.length - 1];
                            lastMessage = lastMsg.message;
                            lastMessageTime = lastMsg.time;
                        }
                    } catch (err) {
                        console.error("Message fetch failed:", err);
                    }

                    return {
                        id: room.id,
                        name: otherUserName,
                        message: lastMessage || "No messages",
                        time: lastMessageTime || "",
                        avatar, // ✅ ADD THIS
                        roomId: room.id,
                    };
                })
            );

            setNotifications(notifs);
        } catch (err) {
            console.error("Failed to fetch notifications:", err);
        }
    };

    // ✅ Polling
    useEffect(() => {
        if (userId) {
            fetchNotifications();
            const interval = setInterval(fetchNotifications, 5000);
            return () => clearInterval(interval);
        } else {
            setNotifications([]);
        }
    }, [userId]);

    // ✅ Close modal on outside click
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (
                modalRef.current &&
                !modalRef.current.contains(event.target)
            ) {
                setModalOpen(false);
            }
        };

        if (modalOpen) {
            document.addEventListener("mousedown", handleClickOutside);
        }

        return () => {
            document.removeEventListener(
                "mousedown",
                handleClickOutside
            );
        };
    }, [modalOpen]);

    // ✅ Navigate to chat
    const handleOpenChat = (roomId) => {
        setModalOpen(false);
        navigate(`/chat/${roomId}`);
    };

    return (
        <div className="relative mx-4">
            {/* 🔔 Bell Icon */}
            <button
                className="relative p-2 rounded-full hover:bg-green-100 transition"
                onClick={() => setModalOpen(!modalOpen)}
            >
                <Bell className="w-6 h-6 text-gray-600" />

                {userId && notifications.length > 0 && (
                    <span className="absolute top-0 right-0 px-2 py-1 text-xs font-bold text-white bg-red-600 rounded-full">
                        {notifications.length}
                    </span>
                )}
            </button>

            {/* 📩 Notifications Modal */}
            {modalOpen && (
                <div
                    ref={modalRef}
                    className="absolute lg:right-0 right-[-2.7em] mt-2 w-80 lg:w-96 max-h-96 overflow-y-auto rounded-lg shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-50"
                >
                    {/* HEADER */}
                    <div className="flex justify-between items-center p-3 border-b">
                        <h3 className="font-semibold text-gray-700">
                            Notifications
                        </h3>
                        <button onClick={() => setModalOpen(false)}>
                            <X className="w-5 h-5 text-gray-500 hover:text-red-700" />
                        </button>
                    </div>

                    {/* BODY */}
                    <div className="p-2">
                        {!userId ? (
                            <p className="text-sm text-gray-500 text-center py-4">
                                Please log in first
                            </p>
                        ) : notifications.length === 0 ? (
                            <p className="text-sm text-gray-500 text-center py-4">
                                No notifications
                            </p>
                        ) : (
                            notifications.map((notif) => (
                                <div
                                    key={notif.id}
                                    className="flex items-start gap-3 p-3 mb-2 hover:bg-gray-100 rounded cursor-pointer"
                                    onClick={() => handleOpenChat(notif.roomId)}
                                >
                                    {/* ✅ AVATAR */}
                                    <img
                                        src={
                                            notif.avatar ||
                                            `https://ui-avatars.com/api/?name=${notif.name}`
                                        }
                                        alt="avatar"
                                        className="w-10 h-10 rounded-full object-cover"
                                    />

                                    {/* ✅ TEXT CONTENT */}
                                    <div className="flex-1">
                                        <div className="flex justify-between items-center">
                                            <p className="text-sm font-semibold text-gray-700">
                                                {notif.name}
                                            </p>
                                            <p className="text-xs text-gray-500">
                                                {notif.time}
                                            </p>
                                        </div>

                                        <p className="text-sm text-left text-green-600 truncate">
                                            {notif.message}
                                        </p>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}