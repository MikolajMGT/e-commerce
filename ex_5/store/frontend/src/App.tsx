import React from "react";
import { ThemeProvider } from "@material-ui/styles";
import {CssBaseline} from "@material-ui/core";
import {MainView} from './components/MainView';
import {withRouter} from 'react-router';
import darkTheme from "./styles/theme";


const App = () => {
	return (
		<ThemeProvider theme={darkTheme}>
			<CssBaseline />
				<MainView/>
		</ThemeProvider>
	);
};

export default withRouter(App);
