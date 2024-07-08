import requests
from bs4 import BeautifulSoup

def check_links(input_file, output_file):
    try:
        with open(input_file, 'r') as file:
            links = file.readlines()
    except FileNotFoundError:
        print(f"Error: The file {input_file} was not found.")
        return

    valid_links = []

    for i, link in enumerate(links):
        link = link.strip()
        print(f"Processing link {i + 1}/{len(links)}: {link}")
        try:
            response = requests.get(link)
            if response.status_code == 200:
                soup = BeautifulSoup(response.text, 'lxml')
                if soup.title and "404" in soup.title.string:
                    print(f"Link {i + 1} is not valid (404 Found in HTML title).")
                elif "404 Not Found" in response.text:
                    print(f"Link {i + 1} is not valid (404 Found in HTML content).")
                else:
                    valid_links.append(link)
                    print(f"############# Link valid [{link}] ###############")
            else:
                print(f"Link {i + 1} returned status code {response.status_code}.")
        except requests.RequestException as e:
            print(f"Error accessing {link}: {e}")

    try:
        with open(output_file, 'w') as file:
            for link in valid_links:
                file.write(link + '\n')
        print(f"Process completed. Valid links saved to {output_file}.")
    except Exception as e:
        print(f"Error writing to file {output_file}: {e}")

# Utilisation
input_file = 'liens_extraits.txt'
output_file = 'liens_valides.txt'
check_links(input_file, output_file)