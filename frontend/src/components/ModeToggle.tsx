import { LaptopMinimal, Moon, Sun } from "lucide-react"

import { Button } from "@/components/ui/button"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { useTheme } from "@/components/ThemeProvider.tsx"

export function ModeToggle() {
    const { setTheme, theme } = useTheme()

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <Button variant="ghost" size="icon">
                    <Sun
                        className={ `absolute h-[1.2rem] w-[1.2rem] scale-0 transition-all ${ theme === 'light' ? 'scale-100 rotate-0' : 'scale-0' }` }/>
                    <Moon
                        className={ `absolute h-[1.2rem] w-[1.2rem] scale-0 transition-all ${ theme === 'dark' ? 'scale-100 rotate-0' : 'scale-0' }` }/>
                    <LaptopMinimal
                        className={ `absolute h-[1.2rem] w-[1.2rem] scale-0 transition-all ${ theme === 'system' ? 'scale-100' : 'scale-0' }` }/>
                    <span className="sr-only">Toggle theme</span>
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
                <DropdownMenuItem onClick={ () => setTheme( "light" ) }>
                    <Sun/> Light
                </DropdownMenuItem>
                <DropdownMenuItem onClick={ () => setTheme( "dark" ) }>
                    <Moon/> Dark
                </DropdownMenuItem>
                <DropdownMenuItem onClick={ () => setTheme( "system" ) }>
                    <LaptopMinimal/> System
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    )
}