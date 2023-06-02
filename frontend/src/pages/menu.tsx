import React, {useEffect} from "react";
import {getAllPizzas} from "../services/pizzaService";
import {PizzaResponse} from "../../gen/api";

export const Menu: React.FunctionComponent = () => {

    const [pizzas, setPizzas] = React.useState<PizzaResponse[]>([]);

    useEffect(() => {
        getAllPizzas().then((pizzas) => {
            setPizzas(pizzas);
        })
    }, [])

    return (
        <div>
            <div className="text-center">
                <h1 className="text-4xl font-bold">Menu</h1>
            </div>
            <div>
                {pizzas.map((pizza) => {
                    return (
                        <div key={pizza.id}>
                            {JSON.stringify(pizza)}
                        </div>
                    )
                })}
            </div>
        </div>
    );
}