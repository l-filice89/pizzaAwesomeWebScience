import React, {useContext} from "react";
import {PizzaCard} from "./pizzaCard";
import {PizzasContext} from "../../pages/customer";

export const Menu: React.FunctionComponent = () => {

    const pizzas = useContext(PizzasContext);

    return (
        <div>
            <div className="text-center">
                <h1 className="text-4xl font-bold">Menu</h1>
            </div>
            <div>
                {pizzas.map((pizza) => {
                    return (
                        <div key={pizza.id} className="m-1">
                            <PizzaCard pizza={pizza} />
                        </div>
                    )
                })}
            </div>
        </div>
    );
}