import React, {useEffect, useState} from "react";
import {getOrders} from "../services/orderService";
import {OrdersGrid} from "../components/ordersGrid";
import {OrderResponse} from "../../gen/api";

export const OrdersContext = React.createContext({
    orders: [] as OrderResponse[],
    setOrders: (orders: OrderResponse[]) => {}
});

export const BeingMadeContext = React.createContext<number>(0);
export const Dashboard: React.FunctionComponent = () => {

    const [orders, setOrders] = useState<OrderResponse[]>([])

    const [ordersBeingMade, setOrdersBeingMade] = React.useState<number>(0);

    useEffect(() => {
        getOrders().then((data) => {
            setOrders(data);
        });
    }, []);

    useEffect(() => {
        setOrdersBeingMade(orders.filter((order) => order.status === "preparing").length);
    }, [orders]);

    return (
        <div>
            <OrdersContext.Provider value={{orders, setOrders}}>
                <BeingMadeContext.Provider value={ordersBeingMade}>
                    <div className="text-center">
                        <h1 className="text-4xl font-bold">Kitchen</h1>
                    </div>
                    <OrdersGrid/>
                </BeingMadeContext.Provider>
            </OrdersContext.Provider>
        </div>
    );
}