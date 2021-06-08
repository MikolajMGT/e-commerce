import styled from 'styled-components';

export const CartItemStyled = styled.div`
	padding: 10px;
	
	.entries-row {
		min-width: 10vw;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
	}
	
	.section {
		min-width: 12vw;
		display: flex;
		flex-direction: column;
		align-items: center;
	}
`;

export const ImageStyled = styled.div<{image: string}>`
	margin-right: 5vw;
	background-image: url(${props => props.image});
	background-size: cover;
	width: 24vh;
	height: 8vw;
	border-radius: 4px;
`
