import React, {FC, useEffect, useState} from 'react';
import {v4 as uuidv4} from 'uuid';
import {Page} from '../../components/Page/Page';
import {Entry, OrderHistoryPageStyled} from './OrderHistoryPageStyled';
import {RootStore} from '../../stores/RootStore';
import {inject, observer} from 'mobx-react';
import {toJS} from 'mobx';
import {IOrder, ProductDetails} from '../../stores/OrdersStore';
import {Col, Row} from 'react-bootstrap';
import {ImageStyled} from '../../components/Cart/CartItem/CartItemStyled';
import {Divider, Typography} from '@material-ui/core';
import HomeIcon from '@material-ui/icons/Home';
import CreditCardIcon from '@material-ui/icons/CreditCard';
import ShoppingBasketIcon from '@material-ui/icons/ShoppingBasket';
import LocalOfferIcon from '@material-ui/icons/LocalOffer';
import AttachMoneyIcon from '@material-ui/icons/AttachMoney';

export const OrderHistoryPage: FC<{ store?: RootStore }> = inject('store')(observer(({store}) => {
    const userStore = store?.userStore;
    const ordersStore = store?.orderStore;

    const [orders, setOrders] = useState<IOrder[]>();

    useEffect(() => {
        if (userStore?.user) {
            ordersStore?.refreshOrders(userStore.user.id);
        }
    }, []);

    useEffect(() => {
        (async () => {
            if (userStore?.user) {
                const result = toJS(await ordersStore?.listOrders());
                console.log(result);
                if (result && result?.length > 0) {
                    setOrders(result);
                }
            }
        })();
    }, [ordersStore?.orders]);

    const prepareOrderSummary = () => {
        return orders?.map((order, index) => {
            return (

                <Entry>
                    <Row>
                        <Typography>Order #{index + 1}</Typography>
                        <Col>
                            <div className="text-center mb-3">
                                <ShoppingBasketIcon style={{paddingRight: '10px'}}/>
                                <Typography variant="button">Bought products</Typography>
                            </div>
                            {prepareProductsSummary(order.products)}
                        </Col>
                        <Col>
                            <Row>
                                <div className="text-center mb-3 mt-1">
                                    <CreditCardIcon style={{paddingRight: '10px'}}/>
                                    <Typography variant="button">Select Credit Card</Typography>
                                    <div>{order.creditCard.cardholderName}</div>
                                    <div className="mb-2">•••• •••• •••• {order.creditCard.number}</div>
                                    <Divider style={{backgroundColor: 'white', width: '94%', marginLeft: '3%'}}/>
                                </div>
                            </Row>
                            <Row>
                                <div className="text-center mt-3 mb-3">
                                    <HomeIcon style={{paddingRight: '10px'}}/>
                                    <Typography variant="button">Delivery Address</Typography>
                                    <div>{order.address.firstname} {order.address.lastname}</div>
                                    <div>{order.address.address}</div>
                                    <div>{order.address.zipcode} {order.address.city}</div>
                                    <div className="mb-2">{order.address.country}</div>
                                    <Divider style={{backgroundColor: 'white', width: '94%', marginLeft: '3%'}}/>
                                </div>
                            </Row>
                            {
                                order.voucher &&
                                <Row>
                                    <div className="text-center mb-3">
                                        <LocalOfferIcon style={{paddingRight: '10px'}}/>
                                        <Typography variant="button">Voucher</Typography>
                                        <div className="mb-2">{order.voucher.code}: -{order.voucher.amount}%</div>
                                        <Divider style={{backgroundColor: 'white', width: '94%', marginLeft: '3%'}}/>
                                    </div>
                                </Row>
                            }
                            <Row>
                                <div className="text-center mb-3">
                                    <AttachMoneyIcon style={{paddingRight: '10px'}}/>
                                    <Typography variant="button">Total</Typography>
                                    <div className="mb-2">{order.payment.amount} PLN</div>
                                    <Divider style={{backgroundColor: 'white', width: '94%', marginLeft: '3%'}}/>
                                </div>
                            </Row>
                        </Col>
                    </Row>
                </Entry>
            );
        });
    };

    const prepareProductsSummary = (products: ProductDetails[]) => {
        return products?.map((product: ProductDetails) => {
            return (
                <Row>
                    <div className="order-summary-item mt-2 mb-2" id={uuidv4()}>
                        <ImageStyled height={'8vh'} width={'12vh'} image={product.imageUrl}/>
                        <div className="m-4">{product.name}</div>
                        <div className="m-4">quantity: {product.quantity}</div>
                        <div className="m-4">{product.price * product.quantity} PLN</div>
                    </div>
                    <Divider style={{backgroundColor: 'white', width: '94%', marginLeft: '3%'}}/>
                </Row>
            );
        });
    };

    return (
        <Page>
            <OrderHistoryPageStyled key={uuidv4()}>
                <div className="entries">
                    {prepareOrderSummary()}
                </div>
            </OrderHistoryPageStyled>
        </Page>
    );
}));
