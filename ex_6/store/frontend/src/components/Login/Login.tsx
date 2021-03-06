import React, {FC} from 'react';
import {
	Button,
	Dialog,
	DialogContent,
	DialogContentText,
	DialogTitle,
	Input, Typography
} from '@material-ui/core';
import {useHistory} from 'react-router';
import {Field, Form, Formik, FormikHelpers} from 'formik';
import { LoginStyled } from './LoginStyled';
import {RootStore} from '../../stores/RootStore';
import {inject, observer} from 'mobx-react';

export interface LoginProps {
	//
}

export const Login: FC<{store?: RootStore}> = inject("store")(observer(({store}) => {
	const history = useHistory();
	const userStore = store?.userStore

	const onSubmit = async (data: {
		email: string,
		password: string,
	}, actions: FormikHelpers<any>) => {
		actions.resetForm();
		await userStore?.authorize()
		history.goBack()
	};

	return (
		<LoginStyled>
			<div className='entries'>
				<Formik initialValues={{
					email: '',
					password: '',
				}}
				        onSubmit={(values, actions) => onSubmit(values, actions)}>
					{({errors, touched, values, isSubmitting}) => (
						<Form className="registerForm">

							<DialogContentText style={{color: "#FCFDFE"}}>Email:</DialogContentText>
							<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE", width: '100%'}} name='email'
							       required
							       error={errors.email && touched.email ? errors.email : null}/>

							<DialogContentText style={{color: "#FCFDFE"}}>Password:</DialogContentText>
							<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE", width: '100%'}} name='password'
							       type="password"
							       required
							       error={errors.password && touched.password ? errors.password : null}/>
							<div className="row">
								<Field as={Button} type='submit' disabled={isSubmitting}
								       color="primary">Sign in</Field>
							</div>
							<div className="row">
								<Typography>Don't have an account?</Typography>
								<Field as={Button} onClick={() => history.push('/register')} disabled={isSubmitting}
								       color="primary">Sign up</Field>
							</div>
						</Form>
					)}
				</Formik>
			</div>
		</LoginStyled>
	);
}));

