import {AppBar, IconButton, Toolbar, Typography} from '@material-ui/core';
import React, {FC} from 'react';
import {useHistory} from 'react-router';
import {RootStore} from '../../stores/RootStore';
import {inject, observer} from 'mobx-react';
import {logoutUser} from '../../services/user';
import Cookies from 'js-cookie';

export const Page: FC<{ store?: RootStore }> = inject('store')(observer(({store, children}) => {
    const userStore = store?.userStore;
    const history = useHistory();

    const logout = async () => {
        console.log(userStore?.user, userStore?.password)
        if (userStore?.user && userStore?.password) {
            try {
                const res = await logoutUser(userStore.user.email, userStore.password)
                console.log(res)
            } catch (e) {
                console.log(e)
            }
        }
        userStore?.clear()
        Cookies.remove('csrfToken')
        Cookies.remove('PLAY_SESSION')
        Cookies.remove('OAuth2State')
        Cookies.remove('authenticator')
        Cookies.remove('userId')
        window.location.reload()
    }

    return (
        <>
            <AppBar position="static" color="secondary">
                <Toolbar>
                    <IconButton edge="start" onClick={() => history.push('/')}>
                        <img height="35px" color="white" src="images/shopping-bag.svg" alt="store"/>
                    </IconButton>
                    <Typography style={{flexGrow: 1, fontWeight: 600}} color="textPrimary">
                        Very Amazing Store
                    </Typography>
                    <div>
                        <IconButton color="inherit" onClick={() => history.push('/cart')}>
                            <img height="35px" src="images/shopping-cart.svg" alt="store"/>
                        </IconButton>
                        <IconButton color="inherit" onClick={() => {
                            console.log(userStore?.user)
                            if (!userStore?.user) {
                                history.push('/history');
                                history.push('/login');
                            } else {
                                history.push('/history');
                            }
                        }}>
                            <img height="35px" src="images/order-list.svg" alt="store"/>
                        </IconButton>
                        <IconButton color="inherit" onClick={() => logout()}>
                            <img height="35px" src="images/logout.svg" alt="store"/>
                        </IconButton>
                    </div>
                </Toolbar>
            </AppBar>
            {children}
        </>
    );
}));
