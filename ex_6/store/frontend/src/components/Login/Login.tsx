import React, {FC} from 'react';
import {
	Button,
	Dialog,
	DialogContent,
	DialogContentText,
	DialogTitle,
	Input,
} from '@material-ui/core';
import {useHistory} from 'react-router';
import {login} from '../../services/user';
import {Field, Form, Formik, FormikHelpers} from 'formik';
import { LoginStyled } from './LoginStyled';

export interface LoginProps {
	//
}

export const Login: FC<LoginProps> = () => {
	const history = useHistory();
	const [open, setOpen] = React.useState(false);

	const handleClickOpen = () => {
		setOpen(true);
	};

	const handleClose = () => {
		setOpen(false);
	};


	const onSubmit = async (data: {
		email: string,
		password: string,
	}, actions: FormikHelpers<any>) => {
		actions.resetForm();
		const res = await login(data.email, data.password);
		history.push('/submit')
	};

	return (
		<LoginStyled>
			<Button onClick={handleClickOpen} className="signUpBtn" color="secondary"> Login to Amazing Store!</Button>
			<Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
				<DialogTitle id="form-dialog-title">Login</DialogTitle>
				<DialogContent>
					<Formik initialValues={{
						email: '',
						password: '',
					}}
					        onSubmit={(values, actions) => onSubmit(values, actions)}>
						{({errors, touched, values, isSubmitting}) => (
							<Form className="registerForm">

								<DialogContentText style={{color: "#FCFDFE"}}>Email:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='email'
								       required
								       error={errors.email && touched.email ? errors.email : null}/>

								<DialogContentText style={{color: "#FCFDFE"}}>Password:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='password'
								       type="password"
								       required
								       error={errors.password && touched.password ? errors.password : null}/>
								<div className="row acceptBtn">
									<Field as={Button} onClick={handleClose} type='submit' disabled={isSubmitting}
									       color="primary">Sign up</Field>
								</div>
							</Form>
						)}
					</Formik>
				</DialogContent>
			</Dialog>
		</LoginStyled>
	);
}

