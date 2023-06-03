import {OrderResponse} from "../../gen/api";
import React, {useContext} from "react";
import {BeingMadeContext} from "../pages/dashboard";

interface iOrderCardProps {
    order: OrderResponse;
}

export const OrderCard: React.FunctionComponent<iOrderCardProps> = ({order}) => {

    const getButtonLabel = (status: string | undefined): string => {
        if (!status) return "";
        switch (status) {
            case "ordered":
                return "Start making";
            case "preparing":
                return "Deliver";
            default:
                return "";
        }
    }

    const getStatusColor = (status: string | undefined): string => {
        let style = "mt-2 font-bold text-center text-white";
        console.log(status);
        if (!status) return style
        switch (status) {
            case "ordered":
                return style.concat(" bg-blue-600");
            case "preparing":
                return style.concat(" bg-yellow-600");
            case "delivered":
                return style.concat(" bg-green-600");
            case "cancelled":
                return style.concat(" bg-red-600");
            default:
                return style;
        }
    }

    const getButtonStyle = (status: string | undefined): string => {
        let style = "text-white font-bold text-center py-2 rounded-b w-full";
        console.log(status);
        if (!status) return style
        switch (status) {
            case "ordered":
                return style.concat(" bg-yellow-600");
            case "preparing":
                return style.concat(" bg-green-600");
            default:
                return style;
        }
    }

    const ordersBeingMade = useContext(BeingMadeContext);

    const changeStatus = (id: number | undefined, status: string | undefined): void => {
        if (!status || !id) return;
        switch (status) {
            case "ordered":
                order.status = "preparing";
                break;
            case "preparing":
                order.status = "delivered";
                break;
            default:
                return;
        }
    }

    return (
        <div key={order.id}
             className="border-2 border-blue-400 rounded flex flex-col">
            <div className="bg-blue-400 text-white font-bold text-center py-2">Order #{order.id}</div>
            <div className="px-1">
                <div>
                    <span className="font-semibold">Customer: </span> {order.customerName}
                </div>
                <div>
                    <span className="font-semibold">Address: </span> {order.customerAddress}
                </div>
                <div>
                    <span className="font-semibold">Phone: </span> {order.customerPhone}
                </div>
            </div>
            <div className={getStatusColor(order.status)}>
                <p>
                    {order.status?.toUpperCase()}
                </p>
            </div>
            {order.status && ((order.status === "ordered" && ordersBeingMade === 0) || order.status === "preparing") &&
                (
                    <div className="flex justify-center">
                        <button className={getButtonStyle(order.status)}
                                onClick={() => changeStatus(order.id, order.status)}>
                            {getButtonLabel(order.status)}
                        </button>
                    </div>
                )
            }
        </div>
    )

}