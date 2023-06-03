import {Header} from "../components/customer/header";
import {Menu} from "../components/customer/menu";
import {Outlet, useNavigate} from "react-router-dom";
import {createContext, useEffect, useState} from "react";
import {PizzaResponse} from "../../gen/api";
import {getAllPizzas} from "../services/pizzaService";
import {Navigation} from "../components/customer/order/navigation";

export const PizzasContext = createContext<PizzaResponse[]>([]);
export const Customer: React.FunctionComponent = () => {

    const nav = useNavigate();

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
                        {/*<div className="w-full flex flex-row justify-center">*/}
                        {/*    <button className="bg-green-600 text-white font-semibold px-2 py-1 rounded my-2 mr-2"*/}
                        {/*            onClick={() => nav("order/place")}>*/}
                        {/*        Place Order*/}
                        {/*    </button>*/}
                        {/*    <button className="bg-green-600 text-white font-semibold px-2 py-1 my-2 rounded"*/}
                        {/*            onClick={() => nav("order/check")}>*/}
                        {/*        Check Order*/}
                        {/*    </button>*/}
                        {/*</div>*/}
                        <Navigation/>
                        <Outlet/>
                    </div>
                </div>
            </PizzasContext.Provider>
        </div>
    );
}