import React from 'react';
import {Redirect, Route, Switch, useRouteMatch} from 'react-router';
import {StorePage} from '../pages/StorePage/StorePage';
import {CartPage} from '../pages/CartPage/CartPage';

export const MainView = () => {

	return (
		<>
			<Routes/>
		</>
	);
};

const Routes = () => {
	const {path} = useRouteMatch();

	return (
		<Switch>
			<Route path={path} exact component={StorePage}/>
			<Route path={path + 'cart'} component={CartPage}/>

			<Route exact path='*'>
				<Redirect to={{pathname: ''}}/>
			</Route>
		</Switch>
	);
};
