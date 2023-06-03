import React from "react";
import {PizzaResponse} from "../../../gen/api";

interface IPizzaCardProps {
    pizza: PizzaResponse
}

export const PizzaCard: React.FunctionComponent<IPizzaCardProps> = ({pizza}) => {
    return (
        <div className="border-2 border-green-600 border-opacity-20 p-1 rounded flex flex-row justify-between items-center">
            <div className="flex flex-col">
                <h4 className="text-xl font-semibold capitalize">{pizza.name}</h4>
                <p className="capitalize">{pizza.ingredients}</p>
            </div>
            <div>
                {pizza.price} $
            </div>
        </div>
    )
}