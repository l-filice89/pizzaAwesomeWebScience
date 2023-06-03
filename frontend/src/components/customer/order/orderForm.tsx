import React, {useContext} from "react";
import {PizzasContext} from "../../../pages/customer";
import {OrderedPizzasContext} from "../placeOrder";
import {OrderFormData} from "../../../model/OrderFormData";
import {FormikErrors, FormikTouched} from "formik";

interface IOrderFormProps {
    handleChange: any;
    handleBlur: any;
    values: OrderFormData,
    touched: FormikTouched<OrderFormData>,
    errors: FormikErrors<OrderFormData>,
    setFieldValue: any
}

export const OrderForm: React.FunctionComponent<IOrderFormProps> = ({
                                                                        handleChange,
                                                                        handleBlur,
                                                                        values,
                                                                        touched,
                                                                        errors,
                                                                        setFieldValue
                                                                    }) => {

    const pizzas = useContext(PizzasContext);

    const orderedPizzasContext = useContext(OrderedPizzasContext);

    const filteredPizzas = pizzas.filter((pizza) => {
        const addedPizzasId = orderedPizzasContext.orderedPizzas.map((pizza) => pizza.pizzaId);
        return !addedPizzasId?.includes(pizza.id);
    });

    return (
        <div className="flex flex-col">
            <form className="flex flex-col p-2">
                <div>
                    <label htmlFor="customerName" className="font-semibold">
                        Name
                    </label>
                    <div className="mt-1">
                        <input
                            type="text"
                            name="customerName"
                            id="customerName"
                            className="border block w-full rounded-md p-1.5 text-gray-900 shadow-sm placeholder:text-gray-400 sm:text-sm sm:leading-6"
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.customerName}
                        />
                    </div>
                    {(touched.customerName && errors.customerName) && (
                        <p className="pl-1 text-sm text-red-600">
                            {errors.customerName}
                        </p>
                    )}
                </div>
                <div>
                    <label htmlFor="customerAddress" className="font-semibold">
                        Address
                    </label>
                    <div className="mt-1">
                        <input
                            type="text"
                            name="customerAddress"
                            id="customerAddress"
                            className="border block w-full rounded-md p-1.5 text-gray-900 shadow-sm placeholder:text-gray-400 sm:text-sm sm:leading-6"
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.customerAddress}
                        />
                    </div>
                    {(touched.customerAddress && errors.customerAddress) && (
                        <p className="pl-1 text-sm text-red-600">
                            {errors.customerAddress}
                        </p>
                    )}
                </div>
                <div>
                    <label htmlFor="customerPhone" className="font-semibold">
                        Phone number
                    </label>
                    <div className="mt-1">
                        <input
                            type="text"
                            name="customerPhone"
                            id="customerPhone"
                            className="border block w-full rounded-md p-1.5 text-gray-900 shadow-sm placeholder:text-gray-400 sm:text-sm sm:leading-6"
                            onChange={handleChange}
                            onBlur={handleBlur}
                            value={values.customerPhone}
                        />
                    </div>
                    {(touched.customerPhone && errors.customerPhone) && (
                        <p className="pl-1 text-sm text-red-600">
                            {errors.customerName}
                        </p>
                    )}
                </div>
                <div className="flex flex-row gap-x-2">
                    <div className="w-2/3">
                        <label htmlFor="pizzaId" className="font-semibold">
                            Pizza
                        </label>
                        <div className="mt-1">
                            <select name="pizzaId"
                                    id="pizzaId"
                                    className="capitalize border block w-full rounded-md p-1.5 text-gray-900 shadow-sm placeholder:text-gray-400 sm:text-sm sm:leading-6"
                                    onChange={handleChange}
                                    value={values.pizzaId}>
                                <option value={0}>Select a pizza</option>
                                {filteredPizzas.map((pizza) => (
                                    <option key={pizza.id} value={pizza.id} className="capitalize">{pizza.name}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                    <div className="w-1/3">
                        <label htmlFor="quantity" className="font-semibold">
                            Quantity
                        </label>
                        <div className="mt-1">
                            <input
                                type="number"
                                name="quantity"
                                id="quantity"
                                value={values.quantity}
                                min={0}
                                className="border block w-full rounded-md p-1.5 text-gray-900 shadow-sm placeholder:text-gray-400 sm:text-sm sm:leading-6"
                                onChange={handleChange}
                            />
                        </div>
                    </div>
                </div>
                <div className="flex justify-end">
                    <button
                        className="bg-green-600 text-white font-semibold px-2 py-1 my-2 rounded w-1/3 disabled:bg-gray-400 disabled:cursor-not-allowed"
                        onClick={(event) => {
                            event.preventDefault();
                            orderedPizzasContext.orderedPizzas.push({
                                pizzaId: Number(values.pizzaId),
                                quantity: Number(values.quantity)
                            })
                            setFieldValue("pizzaId", 0);
                            setFieldValue("quantity", 0);
                        }}
                        disabled={!values.pizzaId || Number(values.pizzaId) === 0 || values.quantity === 0}>
                        Add to order
                    </button>
                </div>
            </form>
        </div>
    );
}