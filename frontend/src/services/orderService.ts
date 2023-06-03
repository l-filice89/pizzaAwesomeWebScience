import {OrderRequest} from "../../gen/api";

export const placeOrder = async (order : OrderRequest) => {
    const response = await fetch('http://localhost:8080/orders', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        mode: 'cors',
        body: JSON.stringify(order)
    });
    return await response.json();
}

export const getOrderById = async (orderId : number) => {
    try {
        const response = await fetch(`http://localhost:8080/orders/${orderId}/customer`);
        return await response.json();
    } catch {
        return {
            status: "Order not found"
        }
    }
}