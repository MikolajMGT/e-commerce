import React, {FC} from 'react';
import {Button, DialogContentText, Input} from '@material-ui/core';
import {useHistory} from 'react-router';
import {Field, Form, Formik, FormikHelpers} from 'formik';
import {RegisterStyled} from './RegisterStyled';
import {RootStore} from '../../stores/RootStore';
import {inject, observer} from 'mobx-react';

export const Register: FC<{ store?: RootStore }> = inject('store')(observer(({store}) => {
    const history = useHistory();
    const userStore = store?.userStore;

    const onSubmit = async (data: {
        email: string,
        password: string,
        repeatPassword: string,
    }, actions: FormikHelpers<any>) => {

        if (data.password !== data.repeatPassword) {
            alert('provided passwords are not the same!')
            return
        }

        actions.resetForm();
        try {
            const res = await userStore?.register(data.email, data.password);
            console.log(res)
            history.push('/order');
        } catch (e) {
            console.log(e)
        }
    };

    return (
        <RegisterStyled>
            <div className="entries">
                <Formik initialValues={{
                    email: '',
                    password: '',
                    repeatPassword: ''
                }}
                        onSubmit={(values, actions) => onSubmit(values, actions)}>
                    {({errors, touched, values, isSubmitting}) => (
                        <Form className="registerForm">

                            <DialogContentText style={{color: '#FCFDFE'}}>Email:</DialogContentText>
                            <Field as={Input} style={{backgroundColor: '#52585D', color: '#FCFDFE', width: '100%'}}
                                   name="email"
                                   required
                                   error={errors.email && touched.email ? errors.email : null}/>

                            <DialogContentText style={{color: '#FCFDFE'}}>Password:</DialogContentText>
                            <Field as={Input} style={{backgroundColor: '#52585D', color: '#FCFDFE', width: '100%'}}
                                   name="password"
                                   type="password"
                                   required
                                   error={errors.password && touched.password ? errors.password : null}/>

                            <DialogContentText style={{color: '#FCFDFE'}}>Repeat Password:</DialogContentText>
                            <Field as={Input} style={{backgroundColor: '#52585D', color: '#FCFDFE', width: '100%'}}
                                   name="repeatPassword"
                                   type="password"
                                   required
                                   error={errors.repeatPassword && touched.repeatPassword ? errors.repeatPassword : null}/>

                            <div className="col">
                                <Field as={Button} type="submit" disabled={isSubmitting}
                                       color="primary">Sign up</Field>
                            </div>
                        </Form>
                    )}
                </Formik>
            </div>
        </RegisterStyled>
    );
}));

