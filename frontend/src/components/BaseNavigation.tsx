import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuLink,
    NavigationMenuList,
} from "@/components/ui/navigation-menu"
import { NavLink, useLocation } from "react-router-dom";
import { Separator } from "@/components/ui/separator.tsx";

type navigationLinkType = {
    name: string;
    href: string;
}

const navigationLinks: navigationLinkType[] = [
    { name: 'Home', href: '/' },
    { name: 'Shop', href: '/shop' },
    { name: 'Warehouses', href: '/warehouse' },
    { name: 'Items', href: '/items' },
];

export default function BaseNavigation( { isMobile, toggleMenu }: { isMobile?: boolean, toggleMenu?: () => void } ) {

    const handleLinkClick = () => {
        if ( isMobile && toggleMenu ) {
            toggleMenu();
        }
    };

    const { pathname } = useLocation();

    return (
        <>
            <NavigationMenu className={ "items-start" } orientation={ `${ isMobile ? `vertical` : `horizontal` }` }>
                <NavigationMenuList className={ `${ isMobile ? `flex-col items-start` : `` }` }>
                    { navigationLinks.map( ( { name, href } ) => (
                        <NavigationMenuItem key={ name }>
                            <NavigationMenuLink data-active={ pathname === href }
                                                className={ "data-[active=true]:text-accent-foreground" } asChild>
                                <NavLink onClick={ handleLinkClick } to={ href }
                                         className="">
                                    { name }
                                </NavLink>
                            </NavigationMenuLink>
                            <Separator orientation={ "vertical" }/>
                        </NavigationMenuItem>
                    ) ) }
                </NavigationMenuList>
            </NavigationMenu>

        </>
    )
}
