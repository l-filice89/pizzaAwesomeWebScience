import {Disclosure} from "@headlessui/react";
import {Link, useLocation} from "react-router-dom";
import {useEffect, useState} from "react";

export const Navigation: React.FunctionComponent = () => {

    const location = useLocation();

    const [section, setSection] = useState<string>("place");

    useEffect(() => {
        const pathElements: string[] = location.pathname.split("/");
        const elementsNumber: number = pathElements.length;
        setSection(pathElements[elementsNumber - 1]);
    }, [location]);

    return (
        <Disclosure as="nav" className="bg-green-200 shadow">
            <div className="relative flex h-10 justify-between">
                <div className="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
                    <div className="hidden sm:ml-6 sm:flex sm:space-x-8 text-gray-900">
                        <Link
                            to="order/place"
                            className={
                                section === "place" ?
                                    "inline-flex items-center font-medium hover:border-b-2 hover:border-green-400 border-b-2 border-green-400" :
                                    "inline-flex items-center font-medium hover:border-b-2 hover:border-green-400"
                            }
                        >
                            Place order
                        </Link>
                        <Link
                            to="order/check"
                            className={
                                section === "check" ?
                                    "inline-flex items-center font-medium hover:border-b-2 hover:border-green-400 border-b-2 border-green-400" :
                                    "inline-flex items-center font-medium hover:border-b-2 hover:border-green-400"
                            }
                        >
                            Check order status
                        </Link>
                    </div>
                </div>
            </div>
        </Disclosure>
    )
}