import {CartStore} from './CartStore';
import {ProductStore} from './ProductStore';

export class RootStore {
	cartStore: CartStore;
	productStore: ProductStore

	constructor() {
		this.cartStore = new CartStore(this);
		this.productStore = new ProductStore(this);
	}
}
