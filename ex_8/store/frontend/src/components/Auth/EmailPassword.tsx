import React, {FC} from 'react';
import {DialogContentText, Input} from '@material-ui/core';
import {Field} from 'formik';

export const EmailPassword: FC<{errors: any, touched: any}> = ({errors, touched}) => {
	return (
		<>
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
		</>
	)
}
