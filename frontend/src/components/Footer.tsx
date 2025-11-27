import { Copyright } from "lucide-react";
import { Separator } from "@/components/ui/separator.tsx";

export default function Footer() {

    return (
        <>
            <Separator/>
            <footer
                className={ `w-full flex-col flex text-foreground text-sm py-2 justify-center items-center gap-2` }>
                <div className={ "flex gap-2 justify-center items-center" }>
                    <Copyright className={ `w-4` }/>
                    <p>Warehouse-App</p>
                </div>
            </footer>
        </>
    )
}
