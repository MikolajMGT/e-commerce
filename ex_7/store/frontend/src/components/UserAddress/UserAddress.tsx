import React, {FC} from 'react';
import CancelIcon from '@material-ui/icons/Cancel';
import {Button, Container, DialogContentText, Input} from '@material-ui/core';
import {Field, Form, Formik, FormikHelpers} from 'formik';
import Box from '@material-ui/core/Box';
import {UserAddressModalStyled} from './UserAddressStyled';
import {Row} from 'react-bootstrap';
import {addUserAddress} from '../../services/userAddress';
import {RootStore} from '../../stores/RootStore';
import {inject, observer} from 'mobx-react';

interface UserAddressModalProps {
    close: any
}

export const UserAddressModal: FC<{ store?: RootStore } & UserAddressModalProps> = inject('store')(observer(({
                                                                                                                 store,
                                                                                                                 close
                                                                                                             }) => {
    const userStore = store?.userStore;
    const addressStore = store?.addressStore;

    const onSubmit = async (data: {
        firstname: string,
        lastname: string,
        address: string,
        zipcode: string,
        city: string,
        country: string,
    }, actions: FormikHelpers<any>) => {

        actions.resetForm();
        if (userStore?.user) {
            try {
                const res = await addUserAddress(userStore.user.id, data.firstname, data.lastname, data.address, data.zipcode, data.city, data.country);
                addressStore?.addAddress(res.data);
                close();
            } catch (error) {
                console.log(error);
            }
        }
    };

    return (
        <UserAddressModalStyled>
            <Box className="cancel"><CancelIcon style={{color: '#d02015'}} onClick={() => close()}/></Box>
            <Container className="content">
                <Row style={{alignItems: 'center', display: 'flex'}}>
                    <Formik initialValues={{
                        firstname: '',
                        lastname: '',
                        address: '',
                        zipcode: '',
                        city: '',
                        country: ''
                    }}
                            onSubmit={(values, actions) => onSubmit(values, actions)}>
                        {({errors, touched, values, isSubmitting}) => (
                            <Form className="registerForm">

                                <DialogContentText style={{color: '#FCFDFE'}}>Firstname:</DialogContentText>
                                <Field as={Input} style={{backgroundColor: '#52585D', color: '#FCFDFE'}}
                                       name="firstname"
                                       required
                                       error={errors.firstname && touched.firstname ? errors.firstname : null}/>

                                <DialogContentText style={{color: '#FCFDFE'}}>Lastname:</DialogContentText>
                                <Field as={Input} style={{backgroundColor: '#52585D', color: '#FCFDFE'}} name="lastname"
                                       required
                                       error={errors.lastname && touched.lastname ? errors.lastname : null}/>

                                <DialogContentText style={{color: '#FCFDFE'}}>Address:</DialogContentText>
                                <Field as={Input} style={{backgroundColor: '#52585D', color: '#FCFDFE'}} name="address"
                                       required
                                       error={errors.address && touched.address ? errors.address : null}/>

                                <DialogContentText style={{color: '#FCFDFE'}}>Zipcode:</DialogContentText>
                                <Field as={Input} style={{backgroundColor: '#52585D', color: '#FCFDFE'}} name="zipcode"
                                       required
                                       error={errors.zipcode && touched.zipcode ? errors.zipcode : null}/>

                                <DialogContentText style={{color: '#FCFDFE'}}>City:</DialogContentText>
                                <Field as={Input} style={{backgroundColor: '#52585D', color: '#FCFDFE'}} name="city"
                                       required
                                       error={errors.city && touched.city ? errors.city : null}/>

                                <DialogContentText style={{color: '#FCFDFE'}}>Country:</DialogContentText>
                                <Field as={Input} style={{backgroundColor: '#52585D', color: '#FCFDFE'}} name="country"
                                       required
                                       error={errors.country && touched.country ? errors.country : null}/>

                                <div className="row acceptBtn">
                                    <Field as={Button} type="submit" disabled={isSubmitting}
                                           color="primary">Save Address</Field>
                                </div>
                            </Form>
                        )}
                    </Formik>
                </Row>
            </Container>
        </UserAddressModalStyled>
    );
}));
