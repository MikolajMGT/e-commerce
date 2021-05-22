import {AppBar, IconButton, Toolbar, Typography} from '@material-ui/core';
import React, {FC} from 'react';
import {useHistory} from 'react-router';

export const Page: FC = ({children}) => {
	const history = useHistory()

	return (
		<>
			<AppBar position="static" color='secondary'>
				<Toolbar>
					<IconButton edge="start" onClick={() => history.push('/')}>
						<img height='35px' color='white' src='images/shopping-bag.svg' alt='store'/>
					</IconButton>
					<Typography style={{flexGrow: 1, fontWeight: 600}} color='textPrimary'>
						Very Amazing Store
					</Typography>
						<div>
							<IconButton color='inherit' onClick={() => history.push('/cart')}>
								<img height='35px' src='images/shopping-cart.svg' alt='store'/>
							</IconButton>
						</div>
				</Toolbar>
			</AppBar>
			{children}
		</>
	);
}
