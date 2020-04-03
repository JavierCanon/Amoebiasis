/*
Copyright Mikhail Samin, 2015.
Only for non-commercial use.

The MIT License (MIT)

Copyright (c) 2015 Mikhail Samin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


If you change this code, please, write to mishasamin@ya.ru (I want to update code when agar.io change).
If you copy or changing the code, this comment should remain unchanged.
*/
function parseString(e,o,t){var n=i18n[e];for(i=0;i<t.length;i++)n=n.replace(o,t[i]);return n}i18n_lang="en",i18n_dict={en:{advertisement:"Advertisement",back_button:"Back",changelog:"Changelog",connect_help:"If you cannot connect to the servers, check if you have some anti virus or firewall blocking the connection.",connecting:"Connecting","continue":"Continue",create_party:"Create",create_party_instructions:"Give this link to your friends:",creating_party:"Creating party...",fb_matchresults_description:"Can you do better? Come and play!",fb_matchresults_subtitle:"I survived %@m %@s and got a score of %@",fb_matchresults_title:"Got a new score in Agar.io",g_plus_share_stats:"I survived %@m %@s and got a score of %@ in Agar.io! Can you do better? Come and play!",gamemode_experimental:"Experimental",gamemode_ffa:"FFA",gamemode_teams:"Teams",games_played:"Games played",highest_mass:"Highest mass",instructions_mouse:"Move your mouse to control your cell",instructions_space:"Press <b>Space</b> to split",instructions_w:"Press <b>W</b> to eject some mass",join_party:"Join",join_party_instructions:"Your friend should have given you a code, type it here:",joined_party_instructions:"You are now playing with this party:",joining_party:"Joining party...",last_match_results:"Last match results",leaderboard:"Leaderboard",leaderboard_time:"Leaderboard Time",login_and_play:"Login and play",login_tooltip:"Login to get your benefits!",logout:"Logout",longest_survival:"Longest survival",mass_eaten:"Mass Eaten",menu_login_facebook:"Sign in with Facebook",menu_login_google:"Sign in with Google",option_dark_theme:"Dark theme",option_no_colors:"No colors",option_no_names:"No names",option_no_skins:"No skins",option_show_mass:"Show mass",option_skip_stats:"Skip stats",party:"Party",party_description:"Play with your friends in the same map",party_join_error:"There was a problem joining that party, please make sure the code is correct, or try creating another party",play:"Play",play_as_guest:"Play as guest",player_cells_eaten:"Player Cells Eaten",position_1:"First",position_10:"Tenth",position_2:"Second",position_3:"Third",position_4:"Fourth",position_5:"Fifth",position_6:"Sixth",position_7:"Seventh",position_8:"Eighth",position_9:"Ninth",privacy_policy:"Privacy Policy",region_australia:"Australia",region_china:"China",region_east_asia:"East Asia",region_europe:"Europe",region_north_america:"North America",region_oceania:"Oceania",region_players:"players",region_poland:"Poland",region_russia:"Russia",region_select:" -- Select a Region -- ",region_south_america:"South America",region_turkey:"Turkey",region_us_east:"US East",region_us_west:"US West",score:"Score",share:"Share",shop:"Shop",spectate:"Spectate",stats:"Stats",stats_cells_eaten:"cells eaten",stats_food_eaten:"food eaten",stats_highest_mass:"highest mass",stats_leaderboard_time:"leaderboard time",stats_time_alive:"time alive",stats_top_position:"top position",survival_time:"Survival Time",terms_of_service:"Terms of Service",top_position:"Top Position",total_cells_eaten:"Total cells eaten",total_mass_eaten:"Total mass eaten",unnamed_cell:"An unnamed cell",option_show_borders:"Show borders",option_green_food:"Green food",option_show_others_mass:"Show opponent's mass",option_extended_colors:"Extended colors",option_extra_zoom:"Extra zoom",custom_skin:"Custom skin...",option_virus_transparency:"Virus transparency",option_large_names:"Large names",option_show_my_range:"Show my range",option_show_virus_range:"Show virus range",option_show_opponents_range:"Show opponent's range",option_merge_time:"Time to remerge",option_no_grid:"No grid",option_last_died:"Last died position",run_bots:"Run bots",option_remember_options:"Remember options",option_follow_my_cell:"Follow my cell",option_follow_my_mouse:"Follow my mouse",option_minimap:"Minimap"}},i18n_lang=(window.navigator.userLanguage||window.navigator.language||"en").split("-")[0],i18n_dict.hasOwnProperty(i18n_lang)||(i18n_lang="en"),i18n=i18n_dict[i18n_lang],window.parseString=parseString;
