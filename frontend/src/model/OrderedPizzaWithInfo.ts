import {OrderedPizzas} from "../../gen/api";

export interface OrderedPizzaWithInfo extends OrderedPizzas{
    name?: string;
    price?: number;
}