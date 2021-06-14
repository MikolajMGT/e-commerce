import styled from 'styled-components';

export const OrderHistoryPageStyled = styled.div`
	display: flex;
	align-items: center;
	justify-content: center;

	.entries {
		margin-top: 40px;
		min-width: 80vw;
		padding: 10px;
		//box-shadow: 0 0 10px black;
		//background-color: rgb(80, 80, 80);
		border-radius: 4px;
	}

	.order-summary-item {
		min-width: 10vw;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
	}
`;

export const Entry = styled.div`
	margin-top: 40px;
	min-width: 80vw;
	padding: 10px;
	box-shadow: 0 0 10px black;
	background-color: rgb(80, 80, 80);
	border-radius: 4px;
`
