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
import {Field, Form, Formik, FormikHelpers} from 'formik';
import {CreditCardStyled} from './CreditCardStyled';
import {addCreditCard} from '../../services/creditCard';

export interface CreditCardProps {
	//
}

export const CreditCard: FC<CreditCardProps> = () => {
	const history = useHistory();
	const [open, setOpen] = React.useState(false);

	const handleClickOpen = () => {
		setOpen(true);
	};

	const handleClose = () => {
		setOpen(false);
	};


	const onSubmit = async (data: {
		cardholderName: string,
		number: string,
		expDate: string,
		cvcCode: string
	}, actions: FormikHelpers<any>) => {
		actions.resetForm();
		const res = await addCreditCard(data.cardholderName, data.number, data.expDate, data.cvcCode);
		history.push('/submit')
	};

	return (
		<CreditCardStyled>
			<Button onClick={handleClickOpen} className="signUpBtn" color="secondary"> Enter your credit card details</Button>
			<Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
				<DialogTitle id="form-dialog-title">Register</DialogTitle>
				<DialogContent>
					<Formik initialValues={{
						cardholderName: '',
						number: '',
						expDate: '',
						cvcCode: ''
					}}
					        onSubmit={(values, actions) => onSubmit(values, actions)}>
						{({errors, touched, values, isSubmitting}) => (
							<Form className="registerForm">

								<DialogContentText style={{color: "#FCFDFE"}}>Cardholder Name:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='cardholderName'
								       required
								       error={errors.cardholderName && touched.cardholderName ? errors.cardholderName : null}/>

								<DialogContentText style={{color: "#FCFDFE"}}>Number:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='number'
								       required
								       error={errors.number && touched.number ? errors.number : null}/>

								<DialogContentText style={{color: "#FCFDFE"}}>Expiration Date:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='expDate'
								       required
								       error={errors.expDate && touched.expDate ? errors.expDate : null}/>

								<DialogContentText style={{color: "#FCFDFE"}}>CVC Code:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='cvcCode'
								       required
								       error={errors.cvcCode && touched.cvcCode ? errors.cvcCode : null}/>

								<div className="row acceptBtn">
									<Field as={Button} onClick={handleClose} type='submit' disabled={isSubmitting}
									       color="primary">Sign up</Field>
								</div>
							</Form>
						)}
					</Formik>
				</DialogContent>
			</Dialog>
		</CreditCardStyled>
	);
}

