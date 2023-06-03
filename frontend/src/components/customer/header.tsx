import React from "react";

export const Header: React.FunctionComponent = () => {
    return (
        <div className="bg-green-600 h-1/6 flex flex-row justify-between items-center px-2 text-white">
            <img className="h-full px-2" src="/pizza.png" alt="logo"/>
            <h1 className="text-6xl font-bold">
                PIZZA AWESOME
            </h1>
            <div className="flex flex-col font-semibold">
                <p>6th Avenue, 109</p>
                <p>New York</p>
                <p>555-123-456</p>
            </div>
        </div>
    )
}