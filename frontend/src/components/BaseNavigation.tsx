import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuLink,
    NavigationMenuList,
} from "@/components/ui/navigation-menu"
import { NavLink, useLocation } from "react-router-dom";

type NavigationLinkType = {
    name: string;
    href: string;
}

const navigationLinks: NavigationLinkType[] = [
    { name: 'Home', href: '/' },
    { name: 'Shop', href: '/shop' },
    { name: 'Warehouses', href: '/warehouse' },
    { name: 'Items', href: '/items' },
];

export default function BaseNavigation( { isMobile, toggleMenu }: Readonly<{
    isMobile?: boolean,
    toggleMenu?: () => void
}> ) {

    const handleLinkClick = () => {
        if ( isMobile && toggleMenu ) {
            toggleMenu();
        }
    };

    const { pathname } = useLocation();

    const orientation = isMobile ? 'vertical' : 'horizontal';

    return (
        <NavigationMenu className={ "items-start" } orientation={ orientation }>
            <NavigationMenuList className={ "flex-col items-start md:flex-row" }>
                { navigationLinks.map( ( { name, href } ) => (
                    <NavigationMenuItem key={ name }>
                        <NavigationMenuLink data-active={ pathname === href }
                                            className={ "data-[active=true]:text-accent-foreground" } asChild>
                            <NavLink onClick={ handleLinkClick } to={ href }>
                                { name }
                            </NavLink>
                        </NavigationMenuLink>
                    </NavigationMenuItem>
                ) ) }
            </NavigationMenuList>
        </NavigationMenu>
    )
}
