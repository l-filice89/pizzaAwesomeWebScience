import React, {useContext} from "react";
import {OrderCard} from "./orderCard";
import {OrdersContext} from "../pages/dashboard";

export const OrdersGrid: React.FunctionComponent = () => {

    const ordersContext = useContext(OrdersContext);

    return (
        <div className="grid grid-cols-5 gap-x-4 gap-y-4 px-5 pt-5">
            {
                ordersContext.orders.map((order) => (
                    <div key={order.id}>
                        <OrderCard order={order}/>
                    </div>
                ))
            }
        </div>
    );
}