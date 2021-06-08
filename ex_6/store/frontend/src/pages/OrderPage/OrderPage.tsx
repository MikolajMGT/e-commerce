import React, {FC} from 'react';
import {v4 as uuidv4} from 'uuid';
import { Page } from '../../components/Page/Page';
import { OrderPageStyled } from './OrderPageStyled';
import {UserAddress} from '../../components/UserAddress/UserAddress';
import { CreditCard } from '../../components/CreditCard/CreditCard';

export const OrderPage: FC = () => {

	return (
		<Page>
			<OrderPageStyled key={uuidv4()}>
				<UserAddress/>
				<CreditCard/>
			</OrderPageStyled>
		</Page>
	);
};
