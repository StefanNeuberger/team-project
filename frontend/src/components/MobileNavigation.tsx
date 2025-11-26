import { Button } from "./ui/button";
import { ArrowLeft, Menu } from "lucide-react";
import { useState } from "react";
import BaseNavigation from "@/components/BaseNavigation.tsx";
import { AnimatePresence, motion } from "framer-motion";

export default function MobileNavigation() {

    const [ isOpen, setIsOpen ] = useState<boolean>( false );

    const handleToggle = () => {
        setIsOpen( prevState => !prevState );
    }

    const handleStopPropagation = ( e: React.MouseEvent ) => {
        e.stopPropagation();
    }

    return (
        <>
            <Button variant={ "outline" } aria-label={ "toggle navigation" }
                    onClick={ handleToggle }>
                <Menu/>
            </Button>
            <AnimatePresence>
                { isOpen &&
                    <div
                        onClick={ handleToggle }
                        className={ "fixed h-full w-full left-0 top-0 bg-transparent backdrop-blur-xs" }>
                        <motion.div
                            onClick={ handleStopPropagation }
                            initial={ { x: "-100%" } }
                            animate={ { x: 0 } }
                            exit={ { x: "-100%" } }
                            transition={ { type: "tween", duration: 0.3 } }
                            className={ "bg-card flex flex-col items-start gap-4 h-full max-w-min p-2 px-4" }>
                            <Button variant={ "ghost" } onClick={ handleToggle }>
                                <ArrowLeft/>
                            </Button>
                            <BaseNavigation isMobile={ true } toggleMenu={ handleToggle }/>
                        </motion.div>
                    </div>
                }
            </AnimatePresence>
        </>
    )
}
