import Footer from "@/components/Footer.tsx";
import { Outlet } from "react-router-dom";
import Header from "@/components/Header.tsx";

export default function RootLayout() {

    return (
        <>
            <Header/>
            <div className={ "flex-1 flex-start flex" }>
                <Outlet/>
            </div>
            <Footer/>
        </>
    )
}
