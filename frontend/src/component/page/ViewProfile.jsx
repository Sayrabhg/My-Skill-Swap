import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUserByEmail, getSkillsByUserId } from "../../api/api";
import { Button } from "@/components/ui/button";

export default function ViewProfile() {

    const { email } = useParams();

    const [user, setUser] = useState(null);
    const [skills, setSkills] = useState([]);

    useEffect(() => {

        const fetchUser = async () => {

            try {

                const res = await getUserByEmail(email);
                const userData = res.data;

                setUser(userData);

                if (userData?.id) {

                    const skillRes = await getSkillsByUserId(userData.id);
                    setSkills(skillRes.data);

                }

            } catch (error) {

                console.error(error);

            }

        };

        fetchUser();

    }, [email]);

    if (!user) return <p className="text-center mt-10">Loading...</p>;

    return (
        <div className="max-w-5xl mx-auto mt-10 px-4">

            {/* PROFILE HEADER */}

            <div className="bg-white shadow-xl rounded-2xl p-6 flex flex-col md:flex-row items-center gap-6">

                <img
                    src={user.avatar || "https://i.pravatar.cc/150"}
                    alt={user.name}
                    className="w-24 h-24 rounded-full"
                />

                <div className="text-center md:text-left">

                    <h2 className="text-2xl font-bold">
                        {user.name}
                    </h2>

                    <p className="text-gray-500">
                        {user.email}
                    </p>

                    <div className="mt-3">
                        <span className="px-3 py-1 bg-indigo-100 text-indigo-600 text-sm rounded-full">
                            SkillSwap Member
                        </span>
                    </div>

                </div>

            </div>

            {/* SKILLS SECTION */}

            <div className="my-8">

                <h3 className="text-xl font-semibold mb-4">
                    Skill Exchange
                </h3>

                {skills.length ? (

                    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-5">

                        {skills.map((skill) => (

                            <div
                                key={skill.id}
                                className="border rounded-xl p-5 hover:shadow-lg transition bg-white"
                            >

                                <div className="flex justify-between items-center mb-3">

                                    <span className="text-xs bg-gray-100 px-2 py-1 rounded">
                                        {skill.category}
                                    </span>

                                </div>

                                <div className="space-y-2">

                                    <p className="text-sm">
                                        <span className="font-semibold text-indigo-600">
                                            Offers:
                                        </span>{" "}
                                        {skill.skillOffered}
                                    </p>

                                    <p className="text-sm">
                                        <span className="font-semibold text-green-600">
                                            Wants:
                                        </span>{" "}
                                        {skill.skillWanted}
                                    </p>

                                </div>

                                <Button
                                    className="w-full mt-4"
                                    size="sm"
                                >
                                    Request Swap
                                </Button>

                            </div>

                        ))}

                    </div>

                ) : (

                    <p className="text-gray-400">
                        No skills added
                    </p>

                )}

            </div>

        </div>
    );
}
