export const getAllPizzas = async () => {
    const response = await fetch('http://localhost:8080/pizzas');
    return  await response.json();
}