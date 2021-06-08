import {createMuiTheme} from '@material-ui/core';


export const darkTheme = createMuiTheme({
	palette : {
		background : {
			default : "#212529",
		},
		text : {
			primary: "#ffffff",
		},
		primary : {
			main : "#4CB944",
			contrastText : "#E5E5E5",
		},
		secondary : {
			main : "#F0803C",
			contrastText : "#E5E5E5",
		},
	},

	//https://fonts.google.com/specimen/Poppins
	typography : {
		fontFamily : "'Poppins', sans-serif",
		h2 : {
			textTransform: "none",
			fontWeight: 600,
		},
		h5 : {
			textTransform: "none",
			fontWeight: 600,
		},
	}

});

export default darkTheme;
