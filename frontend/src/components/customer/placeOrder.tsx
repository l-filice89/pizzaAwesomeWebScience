import React, {createContext} from "react";
import {OrderForm} from "./order/orderForm";
import {OrderedPizzas, OrderRequest} from "../../../gen/api";
import {useFormik} from "formik";
import {OrderFormData} from "../../model/OrderFormData";
import {OrderTable} from "./order/orderTable";
import {placeOrder} from "../../services/orderService";
import {ResultDialog} from "./order/resultDialog";
import * as Yup from "yup";

export const OrderedPizzasContext = createContext({
    orderedPizzas: [] as OrderedPizzas[],
    setOrderedPizzas: (orderedPizzas: OrderedPizzas[]) => {
    }
});
export const PlaceOrder: React.FunctionComponent = () => {

    const [orderedPizzas, setOrderedPizzas] = React.useState<OrderedPizzas[]>([]);

    const initialValues: OrderFormData = {
        customerName: "",
        customerAddress: "",
        pizzaId: 0,
        quantity: 0,
        customerPhone: ""
    };

    const validationSchema = Yup.object().shape({
        customerName: Yup.string().required("Required"),
        customerAddress: Yup.string().required("Required"),
        customerPhone: Yup.string().required("Required"),
    });

    const {
        values,
        handleChange,
        handleBlur,
        touched,
        errors,
        setFieldValue,
        handleSubmit,
        isValid
    } = useFormik({
        initialValues: initialValues,
        onSubmit: () => {
            console.log(values);
            console.log(orderedPizzas);
            const orderRequest: OrderRequest = {
                customerName: values.customerName,
                customerAddress: values.customerAddress,
                customerPhone: values.customerPhone,
                pizzas: orderedPizzas,
                status: "ordered"
            };
            placeOrder(orderRequest).then(r => {
                console.log(r)
                setOrderId(r.id);
                setIsOpen(true);
            });
        },
        validationSchema: validationSchema
    });

    const [isOpen, setIsOpen] = React.useState(false);

    const [orderId, setOrderId] = React.useState(0);

    return (
        <div className="flex flex-col pt-2">
            <div className="flex justify-center">
                <button type="button"
                        className="bg-green-500 hover:bg-green-700 text-white font-semibold py-1 px-2 rounded  disabled:bg-gray-400 disabled:cursor-not-allowed"
                        onClick={() => handleSubmit()}
                        disabled={!values.customerName || !isValid || orderedPizzas.length <= 0}>
                    Place order
                </button>
            </div>
            <div className="flex flex-row justify-between">
                <OrderedPizzasContext.Provider value={{orderedPizzas, setOrderedPizzas}}>
                    <div className="w-1/2">
                        <OrderForm values={values}
                                   errors={errors}
                                   handleBlur={handleBlur}
                                   handleChange={handleChange}
                                   setFieldValue={setFieldValue}
                                   touched={touched}/>
                    </div>
                    <div className="w-1/2">
                        <OrderTable/>
                    </div>
                </OrderedPizzasContext.Provider>
            </div>
            <ResultDialog isOpen={isOpen} setIsOpen={setIsOpen} orderId={orderId}/>
        </div>
    );
}