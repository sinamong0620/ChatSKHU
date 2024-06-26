import Image from 'next/image';
import hunsu from '/public/images/hunsu.png';
import styled from 'styled-components';
import { theme } from '../styles/theme';

const Logo = () => {
    return (
        <>
            <Main_Container>
                <Image src={hunsu} alt="hunsu_img" width={200} height={200} />
                <FontSize>Chat</FontSize>
                <SubTitle>in SKHU</SubTitle>
            </Main_Container>
        </>
    );
};
export default Logo;

const Main_Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`;

const FontSize = styled.p`
    font-size: ${theme.fontSize.logo};
    font-weight: 900;
`;

const SubTitle = styled.p`
    font-weight: 100;
`;
