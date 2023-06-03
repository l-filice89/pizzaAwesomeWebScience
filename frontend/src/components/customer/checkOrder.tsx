import React from "react";
import {useFormik} from "formik";
import {getOrderById} from "../../services/orderService";

interface iValues {
    orderId: number;
}

export const CheckOrder: React.FunctionComponent = () => {

    const initialValues: iValues = {
        orderId: 0
    };

    const [orderStatus, setOrder] = React.useState<String>();

    const {values, handleChange, handleBlur, touched, errors, handleSubmit} = useFormik({
        initialValues: initialValues,
        onSubmit: () => {
            getOrderById(values.orderId).then((response) => {
                response.status ? setOrder(getOrderStatusMessage(response.status)) : setOrder("We couldn't find your order");
            });
        }
    });

    const getOrderStatusMessage = (orderStatus: string): string => {
        switch (orderStatus.toLowerCase()) {
            case "ordered":
                return "Your order has been recorded";
            case "preparing":
                return "Your order is being processed";
            case "delivering":
                return "Your order is on its way";
            case "delivered":
                return "Your order has been delivered";
            case "cancelled":
                return "Your order has been cancelled";
            default:
                return "We couldn't find your order";
        }
    };

    return (
        <div className="text-center">
            <div className="text-center px-2 pt-2">
                <form className="flex flex-row justify-center">
                    <div className="flex flex-row items-center gap-x-2">
                        <label htmlFor="orderId" className="font-semibold">
                            Order ID
                        </label>
                        <div className="mt-1">
                            <input
                                type="number"
                                name="orderId"
                                id="orderId"
                                className="text-right border block w-full rounded-md p-1.5 text-gray-900 shadow-sm placeholder:text-gray-400 sm:text-sm sm:leading-6 [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                                onChange={handleChange}
                                onBlur={handleBlur}
                                value={values.orderId}
                            />
                        </div>
                        {(touched.orderId && errors.orderId) && (
                            <p className="pl-1 text-sm text-red-600">
                                {errors.orderId}
                            </p>
                        )}
                    </div>
                    <button type="button"
                            className="ml-2 bg-green-500 hover:bg-green-700 text-white font-semibold px-2 rounded  disabled:bg-gray-400 disabled:cursor-not-allowed"
                            onClick={() => handleSubmit()}>
                        Check Status
                    </button>
                </form>
            </div>
            <div className="text-xl mt-2 font-bold">
                {orderStatus}
            </div>
        </div>
    )
}