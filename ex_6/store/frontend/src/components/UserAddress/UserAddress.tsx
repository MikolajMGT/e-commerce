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
import {UserAddressStyled} from './UserAddressStyled';
import {addUserAddress} from '../../services/userAddress';

export interface UserAddressProps {
	//
}

export const UserAddress: FC<UserAddressProps> = () => {
	const history = useHistory();
	const [open, setOpen] = React.useState(false);

	const handleClickOpen = () => {
		setOpen(true);
	};

	const handleClose = () => {
		setOpen(false);
	};


	const onSubmit = async (data: {
		firstname: string,
		lastname: string,
		address: string,
		zipcode: string,
		city: string,
		country: string,
	}, actions: FormikHelpers<any>) => {
		actions.resetForm();
		const res = await addUserAddress(data.firstname, data.lastname, data.address, data.zipcode, data.city, data.country);
		history.push('/submit')
	};

	return (
		<UserAddressStyled>
			<Button onClick={handleClickOpen} className="signUpBtn" color="secondary"> Don't have an account? Sign
				Up!</Button>
			<Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
				<DialogTitle id="form-dialog-title">Register</DialogTitle>
				<DialogContent>
					<Formik initialValues={{
						firstname: '',
						lastname: '',
						address: '',
						zipcode: '',
						city: '',
						country: '',
					}}
					        onSubmit={(values, actions) => onSubmit(values, actions)}>
						{({errors, touched, values, isSubmitting}) => (
							<Form className="registerForm">

								<DialogContentText style={{color: "#FCFDFE"}}>Firstname:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='firstname'
								       required
								       error={errors.firstname && touched.firstname ? errors.firstname : null}/>

								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='lastname'
								       required
								       error={errors.lastname && touched.lastname ? errors.lastname : null}/>

								<DialogContentText style={{color: "#FCFDFE"}}>Address:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='address'
								       required
								       error={errors.address && touched.address ? errors.address : null}/>

								<DialogContentText style={{color: "#FCFDFE"}}>Zipcode:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='zipcode'
								       required
								       error={errors.zipcode && touched.zipcode ? errors.zipcode : null}/>

								<DialogContentText style={{color: "#FCFDFE"}}>City:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='city'
								       required
								       error={errors.city && touched.city ? errors.city : null}/>

								<DialogContentText style={{color: "#FCFDFE"}}>Country:</DialogContentText>
								<Field as={Input} style={{backgroundColor: "#52585D", color: "#FCFDFE"}} name='country'
								       required
								       error={errors.country && touched.country ? errors.country : null}/>

								<div className="row acceptBtn">
									<Field as={Button} onClick={handleClose} type='submit' disabled={isSubmitting}
									       color="primary">Sign up</Field>
								</div>
							</Form>
						)}
					</Formik>
				</DialogContent>
			</Dialog>
		</UserAddressStyled>
	);
}

