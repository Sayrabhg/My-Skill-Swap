import { useEffect, useState, useRef } from "react";
import { sendMessage, getChatMessages } from "../../../api/api";
import { Send } from "lucide-react";

import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle
} from "@/components/ui/dialog";

export default function ChatDialog({ open, setOpen, roomId, userId }) {

    const [messages, setMessages] = useState([]);
    const [text, setText] = useState("");
    const bottomRef = useRef(null);

    // Load messages
    const loadMessages = async () => {

        if (!roomId) return;

        try {

            const res = await getChatMessages(roomId);
            setMessages(res.data || []);

        } catch (err) {

            console.error("Failed to load messages:", err);

        }
    };

    // Load messages when dialog opens
    useEffect(() => {

        if (open && roomId) {
            loadMessages();
        }

    }, [open, roomId]);

    // Poll messages every 3 sec
    useEffect(() => {

        if (!open || !roomId) return;

        const interval = setInterval(() => {
            loadMessages();
        }, 3000);

        return () => clearInterval(interval);

    }, [open, roomId]);

    // Auto scroll to bottom
    useEffect(() => {

        bottomRef.current?.scrollIntoView({ behavior: "smooth" });

    }, [messages]);

    // Send message
    const handleSend = async () => {

        if (!text.trim() || !roomId) return;

        try {

            await sendMessage({
                roomId: roomId,
                message: text.trim()
            });

            setText("");

            loadMessages();

        } catch (err) {

            console.error("Send message failed:", err);

        }
    };

    return (

        <Dialog open={open} onOpenChange={setOpen}>

            <DialogContent className="max-w-lg p-0">

                <DialogHeader className="p-4 border-b">
                    <DialogTitle>Chat</DialogTitle>
                </DialogHeader>

                <div className="flex flex-col h-[500px]">

                    {/* Messages */}
                    <div className="flex-1 overflow-y-auto p-4 space-y-3 bg-gray-50">

                        {messages.length === 0 && (
                            <p className="text-center text-gray-400 text-sm">
                                No messages yet
                            </p>
                        )}

                        {messages.map((msg) => (

                            <div
                                key={msg.id}
                                className={`flex ${msg.senderId === userId
                                        ? "justify-end"
                                        : "justify-start"
                                    }`}
                            >

                                <div
                                    className={`px-4 py-2 rounded-xl max-w-xs text-sm shadow
                                    ${msg.senderId === userId
                                            ? "bg-blue-500 text-white"
                                            : "bg-white border"
                                        }`}
                                >

                                    {msg.message}

                                    <div className="text-xs opacity-70 mt-1">
                                        {new Date(msg.timestamp).toLocaleTimeString()}
                                    </div>

                                </div>

                            </div>

                        ))}

                        <div ref={bottomRef}></div>

                    </div>

                    {/* Input */}
                    <div className="border-t p-3 flex gap-2">

                        <input
                            type="text"
                            placeholder="Type a message..."
                            className="flex-1 border rounded-lg px-3 py-2"
                            value={text}
                            onChange={(e) => setText(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === "Enter") {
                                    handleSend();
                                }
                            }}
                        />

                        <button
                            onClick={handleSend}
                            disabled={!text.trim()}
                            className="bg-blue-500 text-white px-4 py-2 rounded-lg flex items-center gap-2 disabled:opacity-50"
                        >
                            <Send size={18} />
                            Send
                        </button>

                    </div>

                </div>

            </DialogContent>

        </Dialog>
    );
}