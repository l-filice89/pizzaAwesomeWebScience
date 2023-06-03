import {Header} from "../components/customer/header";
import {Menu} from "../components/customer/menu";
import {Outlet} from "react-router-dom";
import {createContext, useEffect, useState} from "react";
import {PizzaResponse} from "../../gen/api";
import {getAllPizzas} from "../services/pizzaService";
import {Navigation} from "../components/customer/order/navigation";

export const PizzasContext = createContext<PizzaResponse[]>([]);
export const Customer: React.FunctionComponent = () => {

    const [pizzas, setPizzas] = useState<PizzaResponse[]>([]);

    useEffect(() => {
        getAllPizzas().then((pizzas) => {
            setPizzas(pizzas);
        });
    }, []);

    return (
        <div className="flex flex-col h-screen">
            <Header/>
            <PizzasContext.Provider value={pizzas}>
                <div className="flex flex-row h-full divide-green-600 divide-x-2">
                    <div className="w-1/2 ">
                        <Menu/>
                    </div>
                    <div className="w-1/2 flex flex-col">
                        <Navigation/>
                        <Outlet/>
                    </div>
                </div>
            </PizzasContext.Provider>
        </div>
    );
}