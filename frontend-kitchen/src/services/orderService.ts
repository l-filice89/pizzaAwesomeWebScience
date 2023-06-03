export const getOrders = async () => {
    const response = await fetch('http://localhost:8080/orders');
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