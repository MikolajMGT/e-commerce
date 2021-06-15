import styled from 'styled-components';

export const OrderPageStyled = styled.div`
	display: flex;
	align-items: center;
	justify-content: center;

	.entries {
		min-width: 30vw;
		padding: 10px;
		box-shadow: 0 0 10px black;
		background-color: rgb(80, 80, 80);
		border-radius: 4px;
	}
	
	.order-summary-item {
		min-width: 10vw;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
	}
	
	.order-container {
		
	}
`;
