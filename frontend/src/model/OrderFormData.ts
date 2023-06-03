import {OrderedPizzas} from "../../gen/api";

export interface OrderFormData {
    'customerName'?: string;
    'customerAddress'?: string;
    'customerPhone'?: string;
    'pizzaId'?: number;
    'quantity'?: number;
}