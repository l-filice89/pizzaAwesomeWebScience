import React, {useContext, useEffect, useState} from "react";
import {OrderedPizzasContext} from "../placeOrder";
import {PizzasContext} from "../../../pages/customer";
import {OrderedPizzaWithInfo} from "../../../model/OrderedPizzaWithInfo";
import {MinusSmallIcon, PlusSmallIcon, TrashIcon} from "@heroicons/react/24/outline";

export const OrderTable: React.FunctionComponent = () => {

    const orderedPizzasContext = useContext(OrderedPizzasContext);

    const pizzas = useContext(PizzasContext);

    const [orderedPizzasWithInfo, setOrderedPizzasWithInfo] = useState<OrderedPizzaWithInfo[]>([]);

    useEffect(() => {
        setOrderedPizzasWithInfo(orderedPizzasContext.orderedPizzas.map((orderedPizza) => {
            const pizza = pizzas.find((pizza) => pizza.id === orderedPizza.pizzaId);
            return {
                ...orderedPizza,
                name: pizza?.name,
                price: pizza?.price,
            };
        }));
    }, [orderedPizzasContext, pizzas])

    const removeFromOrder = (pizzaId: number | undefined) => {
        if (!pizzaId) return;
        orderedPizzasContext.setOrderedPizzas(orderedPizzasContext.orderedPizzas.filter((pizza) => pizza.pizzaId !== pizzaId));
    }

    const changeQuantity = (pizzaId: number | undefined, direction: "up" | "down") => {
        if (!pizzaId) return;
        orderedPizzasContext.setOrderedPizzas(orderedPizzasContext.orderedPizzas.map((pizza) => {
            if (pizza.pizzaId === pizzaId) {
                if (!pizza.quantity) pizza.quantity = 0;
                if (direction === "up") {
                    return {
                        ...pizza,
                        quantity: pizza.quantity + 1
                    }
                } else {
                    return {
                        ...pizza,
                        quantity: pizza.quantity - 1
                    }
                }
            } else {
                return pizza;
            }
        }));
    }

    return (
        <div className="px-2">
            <div className="mt-8 flow-root border-2 rounded">
                <table className="min-w-full divide-y divide-gray-300">
                    <thead className="bg-green-200">
                    <tr>
                        <th scope="col"
                            className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6">
                            Name
                        </th>
                        <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                            Quantity
                        </th>
                        <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                            Price
                        </th>
                        <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                        </th>

                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200 bg-white">
                    {orderedPizzasWithInfo.map((pizza) => (
                        <tr key={pizza.pizzaId}>
                            <td className="capitalize whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-6">
                                {pizza.name}
                            </td>
                            <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500 flex flex-row">
                                <MinusSmallIcon onClick={() => {
                                    if (pizza.quantity === 1) {
                                        removeFromOrder(pizza.pizzaId);
                                    } else {
                                        changeQuantity(pizza.pizzaId, "down")
                                    }
                                }} className="mr-2 h-5 w-5 text-red-500 cursor-pointer"/>
                                <div>
                                    {pizza.quantity}
                                </div>
                                <PlusSmallIcon onClick={() => changeQuantity(pizza.pizzaId, "up")}
                                               className="ml-2 h-5 w-5 text-green-500 cursor-pointer"/>
                            </td>
                            <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{pizza.price} $</td>
                            <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">
                                <TrashIcon className="h-5 w-5 text-red-500 cursor-pointer"
                                           aria-hidden="true"
                                           onClick={() => removeFromOrder(pizza.pizzaId)}/>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            <div className="flex flex-row justify-end">
                <p className="font-semibold">TOTAL: </p>
                <p className="ml-2">
                    {orderedPizzasWithInfo.reduce((acc, pizza) => acc + ((pizza.price ? pizza.price : 0) * (pizza.quantity ? pizza.quantity : 0)), 0)} $
                </p>
            </div>
        </div>
    )
};