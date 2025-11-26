import useIsMobile from "@/hooks/useIsMobile.tsx";
import MobileNavigation from "@/components/MobileNavigation.tsx";
import BaseNavigation from "@/components/BaseNavigation.tsx";
import { Separator } from "@/components/ui/separator.tsx";
import { ModeToggle } from "@/components/ModeToggle.tsx";

export default function Header() {
    const { isMobile, isLoading } = useIsMobile();

    if ( isLoading ) return null;

    return (
        <>
            <header className={ "p-2 flex justify-between items-center" }>
                { isMobile ?
                    <MobileNavigation/>
                    :
                    <BaseNavigation/>
                }
                <ModeToggle/>
            </header>
            <Separator/>
        </>
    )
}
